package com.example.evostyle.domain.cart.service;

import com.example.evostyle.domain.cart.dto.request.AddCartItemRequest;
import com.example.evostyle.domain.cart.dto.request.UpdateCartItemRequest;
import com.example.evostyle.domain.cart.dto.response.CartItemResponse;
import com.example.evostyle.domain.cart.dto.response.CartResponse;
import com.example.evostyle.domain.cart.dto.service.RedisCartItemDto;
import com.example.evostyle.domain.cart.entity.Cart;
import com.example.evostyle.domain.cart.entity.CartItem;
import com.example.evostyle.domain.cart.repository.CartItemRepository;
import com.example.evostyle.domain.cart.repository.CartRepository;
import com.example.evostyle.domain.member.entity.Member;
import com.example.evostyle.domain.member.repository.MemberRepository;
import com.example.evostyle.domain.product.productdetail.entity.ProductDetail;
import com.example.evostyle.domain.product.repository.ProductDetailRepository;
import com.example.evostyle.global.exception.ConflictException;
import com.example.evostyle.global.exception.ErrorCode;
import com.example.evostyle.global.exception.NotFoundException;
import com.example.evostyle.global.exception.UnauthorizedException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class MemberCartService {

    private final CartRepository cartRepository;
    private final ProductDetailRepository productDetailRepository;
    private final MemberRepository memberRepository;
    private final CartItemRepository cartItemRepository;
    private final RedisTemplate<String, Object> redisTemplate;

    public final String GUEST_CART_KEY_PREFIX = "guest_cart::";

    @Transactional
    public void addCartItemMember(Long memberId, AddCartItemRequest request) {

        if (!memberRepository.existsById(memberId)) {
            throw new NotFoundException(ErrorCode.MEMBER_NOT_FOUND);
        }

        Cart cart = cartRepository.findByMemberId(memberId).orElseGet(() -> Cart.of(memberId));
        cartRepository.save(cart);

        ProductDetail productDetail = productDetailRepository.findById(request.productDetailId())
                .orElseThrow(() -> new NotFoundException(ErrorCode.PRODUCT_DETAIL_NOT_FOUND));

        if (cartItemRepository.existsByCartIdAndProductDetailId(cart.getId(), productDetail.getId())) {
            throw new ConflictException(ErrorCode.CART_ITEM_ALREADY_EXISTS);
        }

        CartItem cartItem = CartItem.of(cart, productDetail, request.quantity());
        cartItemRepository.save(cartItem);
    }

    public CartResponse readCart(Long memberId) {

        if (!memberRepository.existsById(memberId)) {
            throw new NotFoundException(ErrorCode.MEMBER_NOT_FOUND);
        }

        Cart cart = cartRepository.findByMemberId(memberId)
                .orElseGet(() -> Cart.of(memberId));

        List<CartItemResponse> cartItemResponseList = cartItemRepository.findByCartId(cart.getId())
                .stream().map(CartItemResponse::from).toList();

        return CartResponse.of(cart, cartItemResponseList);
    }


    @Transactional
    public void updateCartItemQuantity(Long memberId, UpdateCartItemRequest request) {

        if (!memberRepository.existsById(memberId)) {
            throw new NotFoundException(ErrorCode.MEMBER_NOT_FOUND);
        }

        Cart cart = cartRepository.findByMemberId(memberId).orElseGet(() -> Cart.of(memberId));
        cartRepository.save(cart);

        CartItem cartItem = cartItemRepository.findByCartIdAndProductDetailId(cart.getId(), request.productDetailId())
                .orElseThrow(() -> new NotFoundException(ErrorCode.CART_ITEM_NOT_FOUND));

        cartItem.updateQuantity(request.quantity());
    }

    @Transactional
    public void deleteCartItem(Long memberId, Long productDetailId) {

        if (!memberRepository.existsById(memberId)) {
            throw new NotFoundException(ErrorCode.MEMBER_NOT_FOUND);
        }

        Cart cart = cartRepository.findByMemberId(memberId).orElseGet(() -> Cart.of(memberId));
        cartRepository.save(cart);

        CartItem cartItem = cartItemRepository.findByCartIdAndProductDetailId(cart.getId(), productDetailId)
                .orElseThrow(() -> new NotFoundException(ErrorCode.CART_ITEM_NOT_FOUND));

        if (cartItem.getCart().getMemberId() != memberId) {
            throw new UnauthorizedException(ErrorCode.CART_ACCESS_DENIED);
        }

        cartItemRepository.deleteById(productDetailId);
    }

    @Transactional
    public CartResponse emptyCart(Long memberId) {
        if (!memberRepository.existsById(memberId)) {
            throw new NotFoundException(ErrorCode.MEMBER_NOT_FOUND);
        }

        Cart cart = cartRepository.findByMemberId(memberId)
                .orElseGet(() -> Cart.of(memberId));

        List<CartItem> cartItems = cartItemRepository.findByCartId(cart.getId());
        cartItemRepository.deleteAll(cartItems);

        return CartResponse.of(cart, new ArrayList<>());
    }


    // 로그인시, 로그인 하지 않은 상태에서 장바구니에 담았던 물건을 회원 장바구니에 머지
    @Transactional
    public CartResponse mergeCart(Long memberId, String cartToken) {

        Cart cart = cartRepository.findByMemberId(memberId).orElseGet(() -> Cart.of(memberId));
        cartRepository.save(cart);

        List<RedisCartItemDto> redisCartItemList = new ArrayList<>(redisTemplate.opsForHash()
                .entries(GUEST_CART_KEY_PREFIX + cartToken).values().stream().map(r -> (RedisCartItemDto) r).toList());

        List<CartItem> dbCartItemList = cartItemRepository.findByCartId(cart.getId());


        for (RedisCartItemDto dto : redisCartItemList) {
            for (CartItem cartItem : dbCartItemList) {

                if (dto.getProductDetailId() == cartItem.getProductDetail().getId()) {
                    cartItem.updateQuantity(cartItem.getQuantity() + dto.getQuantity());
                    redisCartItemList.remove(dto);
                }
            }
        }

        List<CartItem> redisCartItems = redisCartItemList.stream().map(r ->
                CartItem.of(cart, productDetailRepository.getReferenceById(r.getProductDetailId()), r.getQuantity())).toList();

        cartItemRepository.saveAll(redisCartItems);

        List<CartItemResponse> cartItemResponseList = Stream.concat(dbCartItemList.stream(), redisCartItems.stream())
                .map(CartItemResponse::from).toList();

        return CartResponse.of(cart, cartItemResponseList);
    }
}