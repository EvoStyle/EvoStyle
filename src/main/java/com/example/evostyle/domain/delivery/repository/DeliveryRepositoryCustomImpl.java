package com.example.evostyle.domain.delivery.repository;

import com.example.evostyle.domain.brand.entity.QBrand;
import com.example.evostyle.domain.delivery.entity.Delivery;
import com.example.evostyle.domain.delivery.entity.QDelivery;
import com.example.evostyle.domain.member.entity.QMember;
import com.example.evostyle.domain.order.entity.QOrderItem;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;

import java.util.List;

@RequiredArgsConstructor
public class DeliveryRepositoryCustomImpl implements DeliveryRepositoryCustom{

    private final JPAQueryFactory jpaQueryFactory;


    @Override
    public Delivery findWithOrderItemAndBrandAndMemberById(Long deliveryId) {
        QDelivery delivery = QDelivery.delivery;
        return jpaQueryFactory.selectFrom(delivery)
                .join(delivery.brand, QBrand.brand).fetchJoin()
                .join(delivery.orderItems, QOrderItem.orderItem).fetchJoin()
                .join(delivery.member, QMember.member).fetchJoin()
                .where(delivery.id.eq(deliveryId))
                .fetchOne();
    }

    @Override
    public List<Delivery> findAllWithMemberByMemberId(Long memberId) {
        QDelivery delivery = QDelivery.delivery;
        QMember member = QMember.member;
        return jpaQueryFactory.selectFrom(delivery)
                .join(delivery.member,member).fetchJoin()
                .where(member.id.eq(memberId))
                .fetch();
    }

    @Override
    public List<Delivery> findAllWithOrderItemAndMemberByBrandId(Long brandId) {
        QDelivery delivery = QDelivery.delivery;
        QBrand brand = QBrand.brand;
        return jpaQueryFactory.selectFrom(delivery)
                .join(delivery.orderItems,QOrderItem.orderItem).fetchJoin()
                .join(delivery.brand, brand).fetchJoin()
                .where(delivery.brand.id.eq(brandId))
                .fetch();
    }
}
