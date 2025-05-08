package com.example.evostyle.domain.delivery.dto.response;

import com.example.evostyle.domain.delivery.entity.Delivery;
import com.example.evostyle.domain.delivery.entity.DeliveryStatus;
import com.example.evostyle.domain.member.dto.response.MemberResponseForBrand;
import com.example.evostyle.domain.order.dto.response.ReadOrderItemResponse;

import java.time.LocalDateTime;
import java.util.List;

public record DeliveryResponseForBrand(
        DeliveryStatus deliveryStatus,
        String deliveryRequest,
        LocalDateTime createAt,
        LocalDateTime updateAt,
        String address,
        String addressAssistant,
        List<ReadOrderItemResponse> orderItemResponseList,
        MemberResponseForBrand memberResponseForBrand
) {
    public static DeliveryResponseForBrand from(Delivery delivery) {
        return new DeliveryResponseForBrand(
                delivery.getDeliveryStatus(),
                delivery.getDeliveryRequest(),
                delivery.getCreatedAt(),
                delivery.getUpdatedAt(),
                delivery.getDeliveryAddress(),
                delivery.getDeliveryAddressAssistant(),
                delivery.getOrderItems().stream().map(ReadOrderItemResponse::from).toList(),
                MemberResponseForBrand.from(delivery.getMember())
        );
    }

}
