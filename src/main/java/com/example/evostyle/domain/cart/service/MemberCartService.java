package com.example.evostyle.domain.cart.service;

import com.example.evostyle.domain.cart.dto.request.AddCartItemRequest;
import com.example.evostyle.domain.cart.dto.request.UpdateCartItemRequest;
import com.example.evostyle.domain.cart.dto.response.MemberCartItemResponse;
import com.example.evostyle.domain.cart.dto.response.MemberCartResponse;
import com.example.evostyle.domain.cart.dto.service.RedisCartItemDto;
import com.example.evostyle.domain.cart.entity.Cart;
import com.example.evostyle.domain.cart.entity.CartItem;
import com.example.evostyle.domain.cart.repository.CartItemQueryDslRepositoryImp;
import com.example.evostyle.domain.cart.repository.CartItemRepository;
import com.example.evostyle.domain.cart.repository.CartRepository;
import com.example.evostyle.domain.member.entity.Member;
import com.example.evostyle.domain.member.repository.MemberRepository;
import com.example.evostyle.domain.product.dto.response.OptionResponse;
import com.example.evostyle.domain.product.dto.response.ProductDetailResponse;
import com.example.evostyle.domain.product.entity.ProductDetail;
import com.example.evostyle.domain.product.repository.OptionRepository;
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
import java.util.Iterator;
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
    private final CartItemQueryDslRepositoryImp cartItemQueryDslRepository;
    private final RedisTemplate<String, Object> redisTemplate;

    public final String GUEST_CART_KEY_PREFIX = "guest_cart::";


    @Transactional
    public MemberCartItemResponse addCartItem(Long memberId, AddCartItemRequest request) {

        Member member = memberRepository.findById(memberId).orElseThrow(() -> new NotFoundException(ErrorCode.MEMBER_NOT_FOUND));
        Cart cart = cartRepository.findByMemberId(memberId).orElseGet(() -> cartRepository.save(Cart.of(member)));
        ProductDetail productDetail = productDetailRepository.findById(request.productDetailId()).orElseThrow(() -> new NotFoundException(ErrorCode.PRODUCT_DETAIL_NOT_FOUND));

        if (cartItemRepository.existsByCartIdAndProductDetailId(cart.getId(), productDetail.getId())) {
            throw new ConflictException(ErrorCode.CART_ITEM_ALREADY_EXISTS);
        }

        CartItem cartItem = CartItem.of(cart, productDetail, request.quantity());
        cartItemRepository.save(cartItem);

        List<OptionResponse> optionResponseList = optionRepository.findByProductDetailId(productDetail.getId()).stream().map(OptionResponse::from).toList();

        return MemberCartItemResponse.of(
                ProductDetailResponse.from(productDetail, optionResponseList),
                cartItem, member.getMemberGrade());
    }

    public MemberCartResponse readCart(Long memberId) {

        Member member = memberRepository.findById(memberId).orElseThrow(() -> new NotFoundException(ErrorCode.MEMBER_NOT_FOUND));

        if (!memberRepository.existsById(memberId)) {
            throw new NotFoundException(ErrorCode.MEMBER_NOT_FOUND);
        }

        Cart cart = cartRepository.findByMemberId(memberId).orElseGet(() -> cartRepository.save(Cart.of(member)));

        List<MemberCartItemResponse> cartItemResponseList = cartItemQueryDslRepository.findCartItemWithOptions(cart.getId())
                .stream().map(c -> {
                    List<OptionResponse> optionList = optionRepository.findByProductDetailId(c.getProductDetail().getId())
                            .stream().map(OptionResponse::from).toList();

                    return MemberCartItemResponse.of(
                            ProductDetailResponse.from(c.getProductDetail(), optionList),
                            c,
                            member.getMemberGrade());
                }).toList();

        return MemberCartResponse.of(cart, cartItemResponseList);
    }


    @Transactional
    public MemberCartItemResponse updateCartItemQuantity(Long memberId, Long cartItemId, UpdateCartItemRequest request) {

        Member member = memberRepository.findById(memberId).orElseThrow(() -> new NotFoundException(ErrorCode.MEMBER_NOT_FOUND));

        Cart cart = cartRepository.findByMemberId(memberId).orElseGet(() -> Cart.of(member));
        cartRepository.save(cart);

        CartItem cartItem = cartItemRepository.findById(cartItemId).orElseThrow(() -> new NotFoundException(ErrorCode.CART_ITEM_NOT_FOUND));
        cartItem.updateQuantity(request.quantity());

        List<OptionResponse> optionResponseList = optionRepository.findByProductDetailId(cartItem.getProductDetail().getId())
                .stream().map(OptionResponse::from).toList();

        return MemberCartItemResponse.of(
                ProductDetailResponse.from(cartItem.getProductDetail(), optionResponseList),
                cartItem,
                member.getMemberGrade()
        );
    }

    @Transactional
    public void deleteCartItem(Long memberId, Long cartItemId) {

        Member member = memberRepository.findById(memberId).orElseThrow(() -> new NotFoundException(ErrorCode.MEMBER_NOT_FOUND));
        Cart cart = cartRepository.findByMemberId(memberId).orElseGet(() -> cartRepository.save(Cart.of(member)));
        CartItem cartItem = cartItemRepository.findById(cartItemId).orElseThrow(() -> new NotFoundException(ErrorCode.CART_ITEM_NOT_FOUND));

        if (!cartItem.getCart().getMember().getId().equals(memberId)) {
            throw new UnauthorizedException(ErrorCode.CART_ACCESS_DENIED);
        }
        cartItemRepository.deleteById(cartItemId);
    }

    @Transactional
    public MemberCartResponse emptyCart(Long memberId) {
        Member member = memberRepository.findById(memberId).orElseThrow(() -> new NotFoundException(ErrorCode.MEMBER_NOT_FOUND));
        Cart cart = cartRepository.findByMemberId(memberId).orElseGet(() -> Cart.of(member));

        List<CartItem> cartItems = cartItemRepository.findByCartId(cart.getId());
        cartItemRepository.deleteAll(cartItems);

        return MemberCartResponse.of(cart, new ArrayList<>());
    }


    // 로그인시, 로그인 하지 않은 상태에서 장바구니에 담았던 물건을 회원 장바구니에 머지
    @Transactional
    public MemberCartResponse mergeCart(Long memberId, String cartToken) {

        Member member = memberRepository.findById(memberId).orElseThrow(() -> new NotFoundException(ErrorCode.MEMBER_NOT_FOUND));

        Cart cart = cartRepository.findByMemberId(memberId).orElseGet(() -> Cart.of(member));
        cartRepository.save(cart);

        List<RedisCartItemDto> redisCartItemList = new ArrayList<>(redisTemplate.opsForHash()
                .entries(GUEST_CART_KEY_PREFIX + cartToken).values().stream().map(r -> (RedisCartItemDto) r)
                .toList());

        List<CartItem> dbCartItemList = cartItemQueryDslRepository.findCartItemWithOptions(cart.getId());

        Iterator<RedisCartItemDto> iterator = redisCartItemList.iterator();
        while (iterator.hasNext()) {
            RedisCartItemDto dto = iterator.next();

            for (CartItem cartItem : dbCartItemList) {
                if (dto.getProductDetailId().equals(cartItem.getProductDetail().getId())) {
                    cartItem.mergeQuantity(dto.getQuantity());
                    iterator.remove();
                    break;
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

                    return MemberCartItemResponse.of(
                            ProductDetailResponse.from(c.getProductDetail(), optionList),
                            c,
                            member.getMemberGrade());
                }).toList();

        return MemberCartResponse.of(cart, cartItemResponseList);
    }
}