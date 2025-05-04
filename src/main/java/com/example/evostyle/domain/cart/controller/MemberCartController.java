package com.example.evostyle.domain.cart.controller;

import com.example.evostyle.domain.cart.dto.request.AddCartItemRequest;
import com.example.evostyle.domain.cart.dto.request.UpdateCartItemRequest;
import com.example.evostyle.domain.cart.dto.response.MemberCartItemResponse;
import com.example.evostyle.domain.cart.dto.response.MemberCartResponse;
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
    public ResponseEntity<MemberCartItemResponse> addCartItem(@RequestBody AddCartItemRequest request,
                                                              @RequestAttribute(value = "memberId") Long memberId) {

        MemberCartItemResponse cartItemResponse = memberCartService.addCartItem(memberId, request);
        return ResponseEntity.status(HttpStatus.CREATED).body(cartItemResponse);
    }

    @PatchMapping("/cart-items/{cartItemId}")
    public ResponseEntity<MemberCartItemResponse> updateCartItemQuantity(@RequestBody UpdateCartItemRequest request,
                                                                         @PathVariable(name = "cartItemId") Long cartItemId,
                                                                         @RequestAttribute(value = "memberId") Long memberId) {

        MemberCartItemResponse cartItemResponse = memberCartService.updateCartItemQuantity(memberId, cartItemId, request);
        return ResponseEntity.status(HttpStatus.OK).body(cartItemResponse);
    }

    @GetMapping
    public ResponseEntity<MemberCartResponse> readCartByMember(@RequestAttribute(value = "memberId") Long memberId) {

        MemberCartResponse cartResponse = memberCartService.readCart(memberId);
        return ResponseEntity.status(HttpStatus.OK).body(cartResponse);
    }

    @DeleteMapping("/cart-items/{cartItemId}")
    public ResponseEntity<Map<String, Long>> deleteCartItem(@PathVariable(name = "cartItemId") Long cartItemId,
                                                            @RequestAttribute(value = "memberId", required = false) Long memberId) {

        memberCartService.deleteCartItem(memberId, cartItemId);
        return ResponseEntity.status(HttpStatus.OK).body(Map.of("cartItemId", cartItemId));
    }


    @DeleteMapping
    public ResponseEntity<MemberCartResponse> emptyCart(@RequestAttribute(value = "memberId", required = false) Long memberId) {

        MemberCartResponse cartResponse = memberCartService.emptyCart(memberId);
        return ResponseEntity.status(HttpStatus.OK).body(cartResponse);
    }

    @PostMapping
    public ResponseEntity<MemberCartResponse> mergeCart(@RequestAttribute(value = "memberId") Long memberId,
                                                        HttpServletRequest servletRequest,
                                                        HttpServletResponse servletResponse){

        Cookie cookie = CookieUtil.getOrCreateCookie(servletRequest, servletResponse,  GUEST_UUID);
        MemberCartResponse cartResponse = memberCartService.mergeCart(memberId, cookie.getValue());

        return ResponseEntity.status(HttpStatus.OK).body(cartResponse);
    }
}
