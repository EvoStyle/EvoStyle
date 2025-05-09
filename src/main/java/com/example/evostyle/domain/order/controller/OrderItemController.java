package com.example.evostyle.domain.order.controller;

import com.example.evostyle.common.util.JwtUtil;
import com.example.evostyle.domain.order.dto.request.CreateOrderItemRequest;
import com.example.evostyle.domain.order.dto.request.UpdateOrderItemRequest;
import com.example.evostyle.domain.order.dto.response.CreateOrderResponse;
import com.example.evostyle.domain.order.dto.response.UpdateOrderItemResponse;
import com.example.evostyle.domain.order.service.OrderItemService;
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
@RequestMapping("/api")
@RequiredArgsConstructor
public class OrderItemController {

    private final OrderItemService orderItemService;

    @PostMapping("/order-items")
    public ResponseEntity<CreateOrderResponse> createOrder(
            @RequestBody List<CreateOrderItemRequest> requestList,
            @AuthenticationPrincipal AuthUser authUser
    ) {
        CreateOrderResponse createOrderResponse = orderItemService.createOrderItems(requestList, authUser.memberId());

        return ResponseEntity.status(HttpStatus.CREATED).body(createOrderResponse);
    }

    //브랜드별로 주문을 조회할수있게 해야함

    @PatchMapping("/orders/{orderId}/order-items/{orderItemId}")
    public ResponseEntity<UpdateOrderItemResponse> updateOrderItem(
            @RequestBody UpdateOrderItemRequest request,
            @PathVariable(name = "orderId") Long orderId,
            @PathVariable(name = "orderItemId") Long orderItemId,
            @AuthenticationPrincipal AuthUser authUser
    ) {
        UpdateOrderItemResponse response = orderItemService.updateOrderItem(request, orderId, orderItemId, authUser.memberId());

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @DeleteMapping("/orders/{orderId}/order-items/{orderItemId}")
    public ResponseEntity<Map<String, Long>> deleteOrderItem(
            @PathVariable(name = "orderId") Long orderId,
            @PathVariable(name = "orderItemId") Long orderItemId,
            @AuthenticationPrincipal AuthUser authUser
    ) {
        orderItemService.deleteOrderItem(orderId, orderItemId, authUser.memberId());

        return ResponseEntity.status(HttpStatus.OK).body(Map.of("orderItemId", orderItemId));
    }

}
