package com.example.evostyle.domain.cart.controller;

import com.example.evostyle.domain.cart.dto.request.AddCartItemRequest;
import com.example.evostyle.domain.cart.dto.request.UpdateCartItemRequest;
import com.example.evostyle.domain.cart.dto.response.CartItemResponse;
import com.example.evostyle.domain.cart.dto.response.CartResponse;
import com.example.evostyle.domain.cart.entity.Cart;
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
public class MemberCartController {

    private final MemberCartService memberCartService;

    final String GUEST_UUID = "uuid";


    @PostMapping("/cart-items")
    public ResponseEntity<Void> addCartItem(@RequestBody AddCartItemRequest request,
                                            @RequestAttribute(value = "memberId") Long memberId) {

        memberCartService.addCartItemMember(memberId, request);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PatchMapping("/cart-items")
    public ResponseEntity<CartItemResponse> updateCartItemQuantity(@RequestBody UpdateCartItemRequest request,
                                                                   @RequestAttribute(value = "memberId") Long memberId) {

        memberCartService.updateCartItemQuantity(memberId, request);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @GetMapping
    public ResponseEntity<CartResponse> readCartByMember(@RequestAttribute(value = "memberId") Long memberId) {

        CartResponse cartResponse = memberCartService.readCart(memberId);
        return ResponseEntity.status(HttpStatus.OK).body(cartResponse);
    }

    @DeleteMapping("/cart-items/product-details/{productDetailId}")
    public ResponseEntity<Map<String, Long>> deleteCartItem(@PathVariable(name = "productDetailId") Long productDetailId,
                                                            @RequestAttribute(value = "memberId", required = false) Long memberId) {

        memberCartService.deleteCartItem(memberId, productDetailId);
        return ResponseEntity.status(HttpStatus.OK).body(Map.of("productDetailId", productDetailId));
    }


    @DeleteMapping
    public ResponseEntity<CartResponse> emptyCart(@RequestAttribute(value = "memberId", required = false) Long memberId) {

        CartResponse cartResponse = memberCartService.emptyCart(memberId);
        return ResponseEntity.status(HttpStatus.OK).body(cartResponse);
    }

    @PostMapping
    public ResponseEntity<CartResponse> mergeCart(@RequestAttribute(value = "memberId") Long memberId,
                                                  HttpServletRequest servletRequest,
                                                  HttpServletResponse servletResponse){

        Cookie cookie = CookieUtil.getOrCreateCookie(servletRequest, servletResponse,  GUEST_UUID);
        CartResponse cartResponse = memberCartService.mergeCart(memberId, cookie.getValue());

        return ResponseEntity.status(HttpStatus.OK).body(cartResponse);
    }

}
