package com.example.evostyle.domain.order.repository;

import com.example.evostyle.domain.order.entity.OrderItem;
import com.example.evostyle.domain.order.entity.OrderStatus;
import com.example.evostyle.domain.order.entity.QOrderItem;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class OrderItemQueryDslImpl implements OrderItemQueryDsl {
    private final JPAQueryFactory jpaQueryFactory;
    private final QOrderItem orderItem = QOrderItem.orderItem;

    @Override
    public Optional<OrderItem> findById(Long orderItemId) {

        return Optional.ofNullable(
                jpaQueryFactory.selectFrom(orderItem)
                        .where(
                                orderItem.id.eq(orderItemId),
                                orderItem.orderStatus.eq(OrderStatus.PENDING)
                        ).fetchOne()
        );
    }

    @Override
    public List<OrderItem> findByOwnerId(Long memberId) {

        return jpaQueryFactory.selectFrom(orderItem)
                .where(orderItem.brand.member.id.eq(memberId))
                .fetch();
    }
}
