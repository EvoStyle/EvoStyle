package com.example.evostyle.domain.order.repository;

import com.example.evostyle.domain.order.entity.Order;
import com.example.evostyle.domain.order.entity.QOrder;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

import static com.example.evostyle.domain.order.entity.QOrderItem.orderItem;

@Repository
@RequiredArgsConstructor
public class OrderQueryDslImpl implements OrderQueryDsl {
    private final JPAQueryFactory jpaQueryFactory;
    private final QOrder order = QOrder.order;

    @Override
    public Optional<Order> findByIdWithItems(Long orderId) {
        return Optional.ofNullable(
                jpaQueryFactory.selectFrom(order)
                        .leftJoin(order.orderItemList).fetchJoin()
                        .where(order.id.eq(orderId))
                        .fetchOne()
        );
    }

    @Override
    public Optional<Order> findByIdWithItemsAndProductDetail(Long orderId) {
        return Optional.ofNullable(jpaQueryFactory.select(order)
                .from(order)
                .distinct()
                .leftJoin(order.orderItemList, orderItem).fetchJoin()
                .leftJoin(orderItem.productDetail).fetchJoin()
                .where(order.id.eq(orderId))
                .fetchOne()
        );
    }
}
