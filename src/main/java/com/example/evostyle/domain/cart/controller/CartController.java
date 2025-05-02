package com.example.evostyle.domain.cart.controller;

import com.example.evostyle.domain.cart.dto.request.AddCartItemRequest;
import com.example.evostyle.domain.cart.dto.request.UpdateCartItemRequest;
import com.example.evostyle.domain.cart.dto.response.CartItemResponse;
import com.example.evostyle.domain.cart.dto.response.CartResponse;
import com.example.evostyle.domain.cart.service.GuestCartService;
import com.example.evostyle.domain.cart.service.MemberCartService;
import com.example.evostyle.common.util.CookieUtil;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@Slf4j
@RequestMapping("/api/cart")
@RestController
@RequiredArgsConstructor
public class CartController {

    private final GuestCartService guestCartService;
    private final MemberCartService memberCartService;

    final String GUEST_UUID = "uuid";

    @PostMapping("/cart-items")
    public ResponseEntity<Void> addCartItem(@RequestBody AddCartItemRequest request,
                                            @RequestAttribute(value = "memberId", required = false) Long memberId,
                                            HttpServletRequest servletRequest,
                                            HttpServletResponse servletResponse) {

        if (isMember(memberId)) {
            memberCartService.addCartItemMember(memberId, request, request.productDetailId());
        } else {
            Cookie cookie = CookieUtil.getOrCreateCookie(servletRequest, servletResponse, GUEST_UUID);
            guestCartService.addCartItemGuest(request, cookie.getValue(), request.productDetailId());
        }

        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PatchMapping("/cart-items")
    public ResponseEntity<CartItemResponse> updateCartItemQuantity(@RequestBody UpdateCartItemRequest request,
                                                                   @RequestAttribute(value = "memberId", required = false) Long memberId,
                                                                   HttpServletRequest servletRequest,
                                                                   HttpServletResponse servletResponse) {
        if (isMember(memberId)) {
            memberCartService.updateCartItemQuantity(request, request.productDetailId(), memberId);
        } else {
            Cookie cookie = CookieUtil.getOrCreateCookie(servletRequest, servletResponse, GUEST_UUID);
            guestCartService.updateCartItemQuantity(request, cookie.getValue(), request.productDetailId());
        }
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @GetMapping
    public ResponseEntity<CartResponse> readCartByMember(@RequestAttribute(value = "memberId", required = false) Long memberId,
                                                         HttpServletRequest servletRequest,
                                                         HttpServletResponse servletResponse) {

        if (isMember(memberId)) {
            return ResponseEntity.status(HttpStatus.OK).body(memberCartService.readCart(memberId));

        } else {
            Cookie cookie = CookieUtil.getOrCreateCookie(servletRequest, servletResponse, GUEST_UUID);
            return ResponseEntity.status(HttpStatus.OK).body(guestCartService.readCartGuest(cookie.getValue()));
        }

    }

    @DeleteMapping("/cart-items/product-details/{productDetailId}")
    public ResponseEntity<Map<String, Long>> deleteCartItem(@PathVariable(name = "productDetailId") Long productDetailId,
                                                            @RequestAttribute(value = "memberId", required = false) Long memberId,
                                                            HttpServletRequest servletRequest,
                                                            HttpServletResponse servletResponse) {

        if (isMember(memberId)) {
            memberCartService.deleteCartItem(memberId, productDetailId);
        } else {
            Cookie cookie = CookieUtil.getOrCreateCookie(servletRequest, servletResponse, GUEST_UUID);
            guestCartService.deleteCartItem(productDetailId, cookie.getValue());
        }
        return ResponseEntity.status(HttpStatus.OK).body(Map.of("productDetailId", productDetailId));
    }


    @DeleteMapping
    public ResponseEntity<CartResponse> emptyCart(@RequestAttribute(value = "memberId", required = false) Long memberId,
                                                  HttpServletRequest servletRequest,
                                                  HttpServletResponse servletResponse) {

        if(isMember(memberId)){
            return ResponseEntity.status(HttpStatus.OK).body(memberCartService.emptyCart(memberId));
        }else {
            Cookie cookie = CookieUtil.getOrCreateCookie(servletRequest, servletResponse, GUEST_UUID);
            return ResponseEntity.status(HttpStatus.OK).body(guestCartService.emptyCart(cookie.getValue()));
        }
    }

    private boolean isMember(Long memberId) {
        return memberId != null;
    }
}
