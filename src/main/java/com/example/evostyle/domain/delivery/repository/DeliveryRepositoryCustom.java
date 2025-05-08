package com.example.evostyle.domain.delivery.repository;

import com.example.evostyle.domain.delivery.entity.Delivery;

import java.util.List;

public interface DeliveryRepositoryCustom {
    Delivery findWithOrderItemAndBrandAndMemberById(Long deliveryId);

    List<Delivery> findAllWithMemberByMemberId(Long memberId);

    List<Delivery> findAllWithOrderItemAndMemberByBrandId(Long brandId);
}
