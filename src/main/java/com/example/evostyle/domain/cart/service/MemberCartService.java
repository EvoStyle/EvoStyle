package com.example.evostyle.domain.cart.service;

import com.example.evostyle.common.util.MemberDiscountUtil;
import com.example.evostyle.domain.cart.dto.request.AddCartItemRequest;
import com.example.evostyle.domain.cart.dto.request.UpdateCartItemRequest;
import com.example.evostyle.domain.cart.dto.response.MemberCartItemResponse;
import com.example.evostyle.domain.cart.dto.response.MemberCartResponse;
import com.example.evostyle.domain.cart.dto.service.RedisCartItemDto;
import com.example.evostyle.domain.cart.entity.Cart;
import com.example.evostyle.domain.cart.entity.CartItem;
import com.example.evostyle.domain.cart.repository.CartItemRepository;
import com.example.evostyle.domain.cart.repository.CartRepository;
import com.example.evostyle.domain.member.entity.Member;
import com.example.evostyle.domain.member.entity.MemberGradle;
import com.example.evostyle.domain.member.repository.MemberRepository;
import com.example.evostyle.domain.product.dto.response.ProductDetailResponse;
import com.example.evostyle.domain.product.dto.response.ProductResponse;
import com.example.evostyle.domain.product.optiongroup.dto.response.OptionResponse;
import com.example.evostyle.domain.product.optiongroup.entity.Option;
import com.example.evostyle.domain.product.optiongroup.repository.OptionRepository;
import com.example.evostyle.domain.product.productdetail.entity.ProductDetail;
import com.example.evostyle.domain.product.repository.ProductDetailRepository;
import com.example.evostyle.global.exception.ConflictException;
import com.example.evostyle.global.exception.ErrorCode;
import com.example.evostyle.global.exception.NotFoundException;
import com.example.evostyle.global.exception.UnauthorizedException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class MemberCartService {

    private final CartRepository cartRepository;
    private final ProductDetailRepository productDetailRepository;
    private final MemberRepository memberRepository;
    private final CartItemRepository cartItemRepository;
    private final OptionRepository optionRepository;
    private final RedisTemplate<String, Object> redisTemplate;

    public final String GUEST_CART_KEY_PREFIX = "guest_cart::";


    @Transactional
    public MemberCartItemResponse addCartItem(Long memberId, AddCartItemRequest request) {

        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new NotFoundException(ErrorCode.MEMBER_NOT_FOUND));

        Cart cart = cartRepository.findByMemberId(memberId).orElseGet(() -> Cart.of(memberId));
        cartRepository.save(cart);

        ProductDetail productDetail = productDetailRepository.findById(request.productDetailId())
                .orElseThrow(() -> new NotFoundException(ErrorCode.PRODUCT_DETAIL_NOT_FOUND));

        if (cartItemRepository.existsByCartIdAndProductDetailId(cart.getId(), productDetail.getId())) {
            throw new ConflictException(ErrorCode.CART_ITEM_ALREADY_EXISTS);
        }

        CartItem cartItem = CartItem.of(cart, productDetail, request.quantity());
        cartItemRepository.save(cartItem);

        List<OptionResponse> optionResponseList = optionRepository.findByProductDetailId(productDetail.getId())
                .stream().map(OptionResponse::from).toList();

        return MemberCartItemResponse.of(ProductResponse.from(productDetail.getProduct()),
                ProductDetailResponse.from(productDetail, optionResponseList),
                cartItem, member.getMemberGradle());
    }

    public MemberCartResponse readCart(Long memberId) {

        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new NotFoundException(ErrorCode.MEMBER_NOT_FOUND));

        if (!memberRepository.existsById(memberId)) {
            throw new NotFoundException(ErrorCode.MEMBER_NOT_FOUND);
        }

        Cart cart = cartRepository.findByMemberId(memberId)
                .orElseGet(() -> Cart.of(memberId));

        List<MemberCartItemResponse> cartItemResponseList = cartItemRepository.findByCartId(cart.getId())
                .stream().map(c -> {
                    List<OptionResponse> optionList = optionRepository.findByProductDetailId(c.getProductDetail().getId())
                            .stream().map(OptionResponse::from).toList();

                    return MemberCartItemResponse.of(ProductResponse.from(c.getProductDetail().getProduct()),
                            ProductDetailResponse.from(c.getProductDetail(), optionList),
                            c,
                            member.getMemberGradle());
                }).toList();

        return MemberCartResponse.of(cart, cartItemResponseList);
    }


    @Transactional
    public MemberCartItemResponse updateCartItemQuantity(Long memberId, Long cartItemId, UpdateCartItemRequest request) {

        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new NotFoundException(ErrorCode.MEMBER_NOT_FOUND));

        Cart cart = cartRepository.findByMemberId(memberId).orElseGet(() -> Cart.of(memberId));
        cartRepository.save(cart);

        CartItem cartItem = cartItemRepository.findById(cartItemId)
                .orElseThrow(() -> new NotFoundException(ErrorCode.CART_ITEM_NOT_FOUND));

        cartItem.updateQuantity(request.quantity());

        List<OptionResponse> optionResponseList = optionRepository.findByProductDetailId(cartItem.getProductDetail().getId())
                .stream().map(OptionResponse::from).toList();

        return MemberCartItemResponse.of(
                ProductResponse.from(cartItem.getProductDetail().getProduct()),
                ProductDetailResponse.from(cartItem.getProductDetail(), optionResponseList),
                cartItem,
                member.getMemberGradle()
        );
    }

    @Transactional
    public void deleteCartItem(Long memberId, Long cartItemId) {

        if (!memberRepository.existsById(memberId)) {
            throw new NotFoundException(ErrorCode.MEMBER_NOT_FOUND);
        }

        Cart cart = cartRepository.findByMemberId(memberId).orElseGet(() -> Cart.of(memberId));
        cartRepository.save(cart);

        CartItem cartItem = cartItemRepository.findById(cartItemId)
                .orElseThrow(() -> new NotFoundException(ErrorCode.CART_ITEM_NOT_FOUND));

        if (cartItem.getCart().getMemberId() != memberId) {
            throw new UnauthorizedException(ErrorCode.CART_ACCESS_DENIED);
        }
        cartItemRepository.deleteById(cartItemId);
    }

    @Transactional
    public MemberCartResponse emptyCart(Long memberId) {
        if (!memberRepository.existsById(memberId)) {
            throw new NotFoundException(ErrorCode.MEMBER_NOT_FOUND);
        }

        Cart cart = cartRepository.findByMemberId(memberId)
                .orElseGet(() -> Cart.of(memberId));

        List<CartItem> cartItems = cartItemRepository.findByCartId(cart.getId());
        cartItemRepository.deleteAll(cartItems);

        return MemberCartResponse.of(cart, new ArrayList<>());
    }


    // 로그인시, 로그인 하지 않은 상태에서 장바구니에 담았던 물건을 회원 장바구니에 머지
    @Transactional
    public MemberCartResponse mergeCart(Long memberId, String cartToken) {

        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new NotFoundException(ErrorCode.MEMBER_NOT_FOUND));

        Cart cart = cartRepository.findByMemberId(memberId).orElseGet(() -> Cart.of(memberId));
        cartRepository.save(cart);

        List<RedisCartItemDto> redisCartItemList = new ArrayList<>(redisTemplate.opsForHash()
                .entries(GUEST_CART_KEY_PREFIX + cartToken).values().stream().map(r -> (RedisCartItemDto) r)
                .toList());

        List<CartItem> dbCartItemList = cartItemRepository.findByCartId(cart.getId());

        for (RedisCartItemDto dto : redisCartItemList) {
            for (CartItem cartItem : dbCartItemList) {

                if (dto.getProductDetailId() == cartItem.getProductDetail().getId()) {
                    cartItem.mergeQuantity(dto.getQuantity());
                    redisCartItemList.remove(dto);
                }
            }
        }

        List<CartItem> redisCartItems = redisCartItemList.stream().map(r ->
                CartItem.of(cart, productDetailRepository.getReferenceById(r.getProductDetailId()), r.getQuantity())).toList();

        cartItemRepository.saveAll(redisCartItems);

        List<MemberCartItemResponse> cartItemResponseList = Stream.concat(dbCartItemList.stream(), redisCartItems.stream())
                .map(c -> {
                    List<OptionResponse> optionList = optionRepository.findByProductDetailId(c.getProductDetail().getId())
                            .stream().map(OptionResponse::from).toList();

                    return MemberCartItemResponse.of(ProductResponse.from(c.getProductDetail().getProduct()),
                            ProductDetailResponse.from(c.getProductDetail(), optionList),
                            c,
                            member.getMemberGradle());
                }).toList();

        return MemberCartResponse.of(cart, cartItemResponseList);
    }
}