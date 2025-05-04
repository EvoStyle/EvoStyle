package com.example.evostyle.domain.order.repository;

import com.example.evostyle.domain.order.entity.OrderItem;
import com.example.evostyle.domain.order.entity.OrderStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface OrderItemRepository extends JpaRepository<OrderItem, Long> {

    Optional<OrderItem> findByIdAndOrderStatus(Long orderItemId, OrderStatus orderStatus);

    @Query("SELECT oi FROM OrderItem oi WHERE oi.brand.member.id = :memberId")
    List<OrderItem> findByOwnerId(@Param("memberId") Long memberId);

}
