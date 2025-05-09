package com.example.evostyle.domain.order.controller;

import com.example.evostyle.common.util.JwtUtil;
import com.example.evostyle.domain.order.dto.response.ReadOrderResponse;
import com.example.evostyle.domain.order.service.OrderService;
import com.example.evostyle.global.security.AuthUser;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/orders")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;
    private final JwtUtil jwtUtil;

    @GetMapping
    public ResponseEntity<List<ReadOrderResponse>> readAllOrders(@AuthenticationPrincipal AuthUser authUser) {

        List<ReadOrderResponse> orderResponseList = orderService.readAllOrders(authUser.memberId());

        return ResponseEntity.status(HttpStatus.OK).body(orderResponseList);
    }

    @DeleteMapping("/{orderId}")
    public ResponseEntity<Map<String, Long>> deleteOrder(
            @PathVariable(name = "orderId") Long orderId,
            @AuthenticationPrincipal AuthUser authUser
    ) {
        orderService.deleteOrder(orderId, authUser.memberId());

        return ResponseEntity.status(HttpStatus.OK).body(Map.of("orderId", orderId));
    }

}