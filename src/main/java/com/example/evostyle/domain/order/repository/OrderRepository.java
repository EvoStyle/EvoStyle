package com.example.evostyle.domain.order.repository;

import com.example.evostyle.domain.order.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<Order, Long> {
}
