package com.example.evostyle.domain.cart.service;

import com.example.evostyle.domain.cart.dto.request.AddCartItemRequest;
import com.example.evostyle.domain.cart.dto.request.UpdateCartItemRequest;
import com.example.evostyle.domain.cart.dto.response.CartItemResponse;
import com.example.evostyle.domain.cart.dto.response.CartResponse;
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
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class MemberCartService{

    private final CartRepository cartRepository;
    private final ProductDetailRepository productDetailRepository;
    private final MemberRepository memberRepository;
    private final CartItemRepository cartItemRepository;

    @Transactional
    public void addCartItemMember(Long memberId, AddCartItemRequest request, Long productDetailId) {

        //프록시로 받는것이 나을까요?
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new NotFoundException(ErrorCode.MEMBER_NOT_FOUND));

        Cart cart = cartRepository.findByMemberId(memberId).orElseGet(() -> Cart.of(memberId));;
        cartRepository.save(cart);

        ProductDetail productDetail = productDetailRepository.findById(productDetailId)
                .orElseThrow(() -> new NotFoundException(ErrorCode.PRODUCT_DETAIL_NOT_FOUND));

        if (cartItemRepository.existsByCartIdAndProductDetailId(cart.getId(), productDetail.getId())) {
            throw new ConflictException(ErrorCode.CART_ITEM_ALREADY_EXISTS);
        }

        CartItem cartItem = CartItem.of(cart, productDetail, request.quantity());
        cartItemRepository.save(cartItem);
    }

    public CartResponse readCart(Long memberId) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new NotFoundException(ErrorCode.MEMBER_NOT_FOUND));

        Cart cart = cartRepository.findByMemberId(memberId)
                .orElseGet(() -> Cart.of(memberId));

        List<CartItemResponse> cartItemResponseList = cartItemRepository.findByCartId(cart.getId())
                .stream().map(CartItemResponse::from).toList();

        return CartResponse.of(cart, cartItemResponseList);
    }


    @Transactional
    public void updateCartItemQuantity(UpdateCartItemRequest request, Long productDetailId, Long memberId) {

        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new NotFoundException(ErrorCode.MEMBER_NOT_FOUND));

        Cart cart = cartRepository.findByMemberId(memberId).orElseGet(() -> Cart.of(memberId));
        cartRepository.save(cart);

        CartItem cartItem = cartItemRepository.findByCartIdAndProductDetailId(cart.getId(), productDetailId)
                .orElseThrow(() -> new NotFoundException(ErrorCode.CART_ITEM_NOT_FOUND));

        cartItem.updateQuantity(request.quantity());
    }

    @Transactional
    public void deleteCartItem(Long memberId, Long productDetailId) {

        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new NotFoundException(ErrorCode.MEMBER_NOT_FOUND));

        Cart cart = cartRepository.findByMemberId(memberId).orElseGet(() -> Cart.of(memberId));
        cartRepository.save(cart);

        CartItem cartItem = cartItemRepository.findByCartIdAndProductDetailId(cart.getId(),productDetailId)
                .orElseThrow(() -> new NotFoundException(ErrorCode.CART_ITEM_NOT_FOUND));

        if (cartItem.getCart().getMemberId() != memberId) {
            throw new UnauthorizedException(ErrorCode.CART_ACCESS_DENIED);
        }

        cartItemRepository.deleteById(productDetailId);
    }

    @Transactional
    public CartResponse emptyCart(Long memberId) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new NotFoundException(ErrorCode.MEMBER_NOT_FOUND));

        Cart cart = cartRepository.findByMemberId(memberId)
                .orElseGet(() -> Cart.of(memberId));

        List<CartItem> cartItems = cartItemRepository.findByCartId(cart.getId());
        cartItemRepository.deleteAll(cartItems);

        return CartResponse.of(cart, new ArrayList<>());
    }

}