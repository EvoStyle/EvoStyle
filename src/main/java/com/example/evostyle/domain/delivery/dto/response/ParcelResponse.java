package com.example.evostyle.domain.delivery.dto.response;

import com.example.evostyle.domain.parcel.entity.Parcel;
import com.example.evostyle.domain.parcel.entity.ParcelStatus;

import java.time.LocalDateTime;

public record ParcelResponse(
        String trackingNumber,
        String senderName,
        String receiverName,
        String receiverAddress,
        String receiverPhone,
        String receiverPostCode,
        ParcelStatus parcelStatus,
        LocalDateTime estimatedDeliveryDate,
        LocalDateTime createdAt
) {
    public static ParcelResponse from(Parcel parcel) {
        return new ParcelResponse(
                parcel.getTrackingNumber(),
                parcel.getSender().getName(),
                parcel.getReceiver().getName(),
                parcel.getReceiver().getAddress(),
                parcel.getReceiver().getPhone(),
                parcel.getReceiver().getPostCode(),
                parcel.getParcelStatus(),
                parcel.getEstimatedDeliveryDate(),
                parcel.getCreatedAt());
    }
}
