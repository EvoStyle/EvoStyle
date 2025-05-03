package com.example.evostyle.domain.cart.controller;


import com.example.evostyle.common.util.CookieUtil;
import com.example.evostyle.domain.cart.dto.request.AddCartItemRequest;
import com.example.evostyle.domain.cart.dto.request.UpdateCartItemRequest;
import com.example.evostyle.domain.cart.dto.response.CartItemResponse;
import com.example.evostyle.domain.cart.dto.response.CartResponse;
import com.example.evostyle.domain.cart.service.GuestCartService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;


@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class GuestCartController {

    private final GuestCartService guestCartService;

    final String GUEST_UUID = "uuid";

    @PostMapping("/cart-items")
    public ResponseEntity<Void> addCartItem(@RequestBody AddCartItemRequest request,
                                            HttpServletRequest servletRequest,
                                            HttpServletResponse servletResponse) {

       Cookie cookie = CookieUtil.getOrCreateCookie(servletRequest, servletResponse, GUEST_UUID);

        guestCartService.addCartItemGuest(request, cookie.getValue());
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PatchMapping("/cart-items")
    public ResponseEntity<CartItemResponse> updateCartItemQuantity(@RequestBody UpdateCartItemRequest request,
                                                                  HttpServletRequest servletRequest,
                                                                   HttpServletResponse servletResponse) {

        Cookie cookie = CookieUtil.getOrCreateCookie(servletRequest, servletResponse, GUEST_UUID);

        guestCartService.updateCartItemQuantity(cookie.getValue(), request);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @GetMapping
    public ResponseEntity<CartResponse> readCartByMember(HttpServletRequest servletRequest,
                                                         HttpServletResponse servletResponse) {

        Cookie cookie = CookieUtil.getOrCreateCookie(servletRequest, servletResponse, GUEST_UUID);

        CartResponse cartResponse = guestCartService.readCartGuest(cookie.getValue());
        return ResponseEntity.status(HttpStatus.OK).body(cartResponse);
    }

    @DeleteMapping("/cart-items/product-details/{productDetailId}")
    public ResponseEntity<Map<String, Long>> deleteCartItem(@PathVariable(name = "productDetailId") Long productDetailId,
                                                            HttpServletRequest servletRequest,
                                                            HttpServletResponse servletResponse) {

        Cookie cookie = CookieUtil.getOrCreateCookie(servletRequest, servletResponse, GUEST_UUID);

        guestCartService.deleteCartItem(cookie.getValue(), productDetailId);
        return ResponseEntity.status(HttpStatus.OK).body(Map.of("productDetailId", productDetailId));
    }


    @DeleteMapping
    public ResponseEntity<CartResponse> emptyCart(HttpServletRequest servletRequest,
                                                  HttpServletResponse servletResponse) {

        Cookie cookie = CookieUtil.getOrCreateCookie(servletRequest, servletResponse, GUEST_UUID);

        CartResponse cartResponse = guestCartService.emptyCart(cookie.getValue());
        return ResponseEntity.status(HttpStatus.OK).body(cartResponse);
    }

}
