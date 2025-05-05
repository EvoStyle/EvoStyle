package com.example.evostyle.domain.order.repository;

import com.example.evostyle.domain.order.entity.OrderItem;

import java.util.List;
import java.util.Optional;

public interface OrderItemQueryDsl {
    Optional<OrderItem> findPendingById(Long orderItemId);

    List<OrderItem> findByOwnerId(Long memberId);
}
