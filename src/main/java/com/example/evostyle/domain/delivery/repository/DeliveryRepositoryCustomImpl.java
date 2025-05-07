package com.example.evostyle.domain.delivery.repository;

import com.example.evostyle.domain.brand.entity.QBrand;
import com.example.evostyle.domain.delivery.entity.Delivery;
import com.example.evostyle.domain.delivery.entity.QDelivery;
import com.example.evostyle.domain.member.entity.QMember;
import com.example.evostyle.domain.order.entity.QOrderItem;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class DeliveryRepositoryCustomImpl implements DeliveryRepositoryCustom{

    private final JPAQueryFactory jpaQueryFactory;


    @Override
    public Delivery findWithOrderItemAndBrandAndMemberById(Long deliveryId) {
        QDelivery delivery = QDelivery.delivery;
        return jpaQueryFactory.selectFrom(delivery)
                .leftJoin(delivery.brand, QBrand.brand).fetchJoin()
                .leftJoin(delivery.orderItem, QOrderItem.orderItem).fetchJoin()
                .leftJoin(delivery.member, QMember.member).fetchJoin()
                .where(delivery.id.eq(deliveryId))
                .fetchOne();
    }
}
