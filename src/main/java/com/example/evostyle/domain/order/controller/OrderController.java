package com.example.evostyle.domain.order.controller;

import com.example.evostyle.domain.order.dto.request.CreateOrderItemRequest;
import com.example.evostyle.domain.order.dto.response.CreateOrderResponse;
import com.example.evostyle.domain.order.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/orders")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    @PostMapping
    public ResponseEntity<CreateOrderResponse> createOrder(@RequestBody List<CreateOrderItemRequest> requestList) {

        CreateOrderResponse createOrderResponse = orderService.createOrder(requestList);

        return ResponseEntity.status(HttpStatus.CREATED).body(createOrderResponse);
    }

//    @GetMapping
//    public ResponseEntity<List<ReadOrderItemWrapper>> readOrders() {
//
//        List<ReadOrderItemWrapper> wrapperList = orderService.readAllOrders();
//
//        return ResponseEntity.status(HttpStatus.OK).body(wrapperList);
//    }
}
