package com.example.evostyle.domain.order.repository;

import com.example.evostyle.domain.order.entity.OrderItem;
import com.example.evostyle.domain.order.entity.OrderStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface OrderItemRepository extends JpaRepository<OrderItem, Long> {

    @Query("SELECT oi FROM OrderItem oi " +
            "JOIN FETCH oi.productDetail pd " +
            "JOIN FETCH pd.product p " +
            "JOIN FETCH p.brand b " +
            "WHERE b.id IN :brandIdList " +
            "AND oi.orderStatus = 'PENDING'")
    List<OrderItem> findOrderItemsByBrandIdList(@Param("brandIdList") List<Long> brandIdList);

    Optional<OrderItem> findByIdAndOrderStatus(Long orderItemId, OrderStatus orderStatus);

    boolean existsByOrderIdAndIsCancelledFalse(Long orderId);
}
