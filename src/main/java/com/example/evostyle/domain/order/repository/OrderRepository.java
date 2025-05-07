package com.example.evostyle.domain.order.repository;

import com.example.evostyle.domain.order.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface OrderRepository extends JpaRepository<Order, Long> {

    @Query("""
                SELECT o FROM Order o
                JOIN FETCH o.orderItemList oi
                JOIN FETCH oi.productDetail
                JOIN FETCH o.member
                WHERE o.id = :orderId
            """)
    Order findOrderWithDetails(@Param("orderId") Long orderId);

}
