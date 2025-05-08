package com.example.evostyle.domain.cart.controller;


import com.example.evostyle.common.util.CookieUtil;
import com.example.evostyle.domain.cart.dto.request.AddCartItemRequest;
import com.example.evostyle.domain.cart.dto.request.UpdateCartItemRequest;
import com.example.evostyle.domain.cart.dto.response.GuestCartItemResponse;
import com.example.evostyle.domain.cart.dto.response.GuestCartResponse;
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
@RequestMapping("/api/guest-cart")
@RequiredArgsConstructor
public class GuestCartController {

    private final GuestCartService guestCartService;

    final String GUEST_UUID = "uuid";

    @PostMapping("/cart-items")
    public ResponseEntity<GuestCartItemResponse> addCartItem(@RequestBody AddCartItemRequest request,
                                                             HttpServletRequest servletRequest,
                                                             HttpServletResponse servletResponse) {

       Cookie cookie = CookieUtil.getOrCreateCookie(servletRequest, servletResponse, GUEST_UUID);

        GuestCartItemResponse response = guestCartService.addCartItem(request, cookie.getValue());
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PatchMapping("/cart-items/{productDetailId}")
    public ResponseEntity<GuestCartItemResponse> updateCartItemQuantity(@PathVariable(name = "productDetailId") Long productDetailId,
                                                                        @RequestBody UpdateCartItemRequest request,
                                                                         HttpServletRequest servletRequest,
                                                                         HttpServletResponse servletResponse) {

        Cookie cookie = CookieUtil.getOrCreateCookie(servletRequest, servletResponse, GUEST_UUID);

        GuestCartItemResponse response = guestCartService.updateCartItemQuantity(cookie.getValue(), request, productDetailId);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @GetMapping
    public ResponseEntity<GuestCartResponse> readCartByCookie(HttpServletRequest servletRequest,
                                                               HttpServletResponse servletResponse) {

        Cookie cookie = CookieUtil.getOrCreateCookie(servletRequest, servletResponse, GUEST_UUID);

        GuestCartResponse cartResponse = guestCartService.readCart(cookie.getValue());
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
    public ResponseEntity<GuestCartResponse> emptyCart(HttpServletRequest servletRequest,
                                                        HttpServletResponse servletResponse) {

        Cookie cookie = CookieUtil.getOrCreateCookie(servletRequest, servletResponse, GUEST_UUID);

        GuestCartResponse cartResponse = guestCartService.emptyCart(cookie.getValue());
        return ResponseEntity.status(HttpStatus.OK).body(cartResponse);
    }

}
