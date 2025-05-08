package com.example.evostyle.domain.order.repository;

import com.example.evostyle.domain.order.entity.Order;

import java.util.Optional;

public interface OrderQueryDsl {

    Optional<Order> findByIdWithItems(Long orderId);
}
