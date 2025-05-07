package com.example.evostyle.domain.delivery.repository;

import com.example.evostyle.domain.delivery.entity.Delivery;

public interface DeliveryRepositoryCustom {
    Delivery findWithOrderItemAndBrandAndMemberById(Long deliveryId);
}
