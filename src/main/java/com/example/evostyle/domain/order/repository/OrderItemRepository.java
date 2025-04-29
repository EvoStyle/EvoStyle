package com.example.evostyle.domain.order.repository;

import com.example.evostyle.domain.order.entity.OrderItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface OrderItemRepository extends JpaRepository<OrderItem, Long> {

    @Query("SELECT oi FROM OrderItem oi " +
            "JOIN FETCH oi.productDetail pd " +
            "JOIN FETCH pd.product p " +
            "JOIN FETCH p.brand b " +
            "WHERE b.id IN :brandIdList")
    List<OrderItem> findOrderItemsByBrandIdList(@Param("brandIdList") List<Long> brandIdList);
}
