package com.example.evostyle.domain.order.controller;

import com.example.evostyle.common.util.JwtUtil;
import com.example.evostyle.domain.order.dto.request.CreateOrderItemRequest;
import com.example.evostyle.domain.order.dto.request.UpdateOrderItemRequest;
import com.example.evostyle.domain.order.dto.response.CreateOrderResponse;
import com.example.evostyle.domain.order.dto.response.UpdateOrderItemResponse;
import com.example.evostyle.domain.order.service.OrderItemService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/orders")
@RequiredArgsConstructor
public class OrderItemController {

    private final OrderItemService orderItemService;
    private final JwtUtil jwtUtil;

    @PostMapping
    public ResponseEntity<CreateOrderResponse> createOrder(
            @RequestBody List<CreateOrderItemRequest> requestList,
            HttpServletRequest httpServletRequest
    ) {
        Long memberId = extractMemberId(httpServletRequest);

        CreateOrderResponse createOrderResponse = orderItemService.createOrderItems(requestList, memberId);

        return ResponseEntity.status(HttpStatus.CREATED).body(createOrderResponse);
    }

    @PatchMapping("/{orderId}/order-items/{orderItemId}")
    public ResponseEntity<UpdateOrderItemResponse> updateOrderItem(
            @RequestBody UpdateOrderItemRequest request,
            @PathVariable(name = "orderId") Long orderId,
            @PathVariable(name = "orderItemId") Long orderItemId,
            HttpServletRequest httpServletRequest
    ) {
        Long memberId = extractMemberId(httpServletRequest);

        UpdateOrderItemResponse response = orderItemService.updateOrderItem(
                request,
                orderId,
                orderItemId,
                memberId
        );

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @DeleteMapping("/{orderId}/order-items/{orderItemId}")
    public ResponseEntity<Map<String, Long>> deleteOrderItem(
            @PathVariable(name = "orderId") Long orderId,
            @PathVariable(name = "orderItemId") Long orderItemId,
            HttpServletRequest httpServletRequest
    ) {
        Long memberId = extractMemberId(httpServletRequest);

        orderItemService.deleteOrderItem(orderId, orderItemId, memberId);

        return ResponseEntity.status(HttpStatus.OK).body(Map.of("orderItemId", orderItemId));
    }

    private Long extractMemberId(HttpServletRequest httpServletRequest) {
        return jwtUtil.getMemberId(httpServletRequest.getHeader("Authorization"));
    }
}
