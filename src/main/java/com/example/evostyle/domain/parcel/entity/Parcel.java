package com.example.evostyle.domain.parcel.entity;

import com.example.evostyle.common.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.concurrent.ThreadLocalRandom;

@Entity
@Getter
@Table(name = "parcel")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Parcel extends BaseEntity {

    @Id
    private String trackingNumber;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "sender_id")
    private Sender sender;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "receiver_id")
    private Receiver receiver;

    private ParcelStatus parcelStatus;

    private String deliveryRequest;

    private LocalDateTime estimatedDeliveryDate;

    private Parcel(String trackingNumber, Sender sender, Receiver receiver, ParcelStatus parcelStatus,String deliveryRequest) {
        this.trackingNumber = trackingNumber;
        this.sender = sender;
        this.receiver = receiver;
        this.parcelStatus = parcelStatus;
        this.deliveryRequest = deliveryRequest;
        this.estimatedDeliveryDate = LocalDateTime.now().plusDays(ThreadLocalRandom.current().nextInt(1,5));
    }

    public static Parcel of(String tracking_number, Sender sender, Receiver receiver,String deliveryRequest) {
        return  new Parcel(tracking_number,sender,receiver,ParcelStatus.ISSUED,deliveryRequest);
    }
}
