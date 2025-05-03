package com.example.evostyle.domain.delivery.entity;

import com.example.evostyle.domain.member.entity.Member;
import com.example.evostyle.domain.order.entity.OrderItem;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Table(name = "deliveries")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Delivery {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_item_id", nullable = false)
    private OrderItem orderItem;

    @Enumerated(EnumType.STRING)
    @Column(name = "delivery_status",nullable = false)
    private DeliveryStatus deliveryStatus;

    @Column(name = "delivery_request",length = 100)
    private String deliveryRequest;

    @Column(name = "delivery_address",nullable = false,length = 150)
    private String deliveryAddress;

    @Column(name = "delivery_address_assistant",nullable = false,length = 100)
    private String deliveryAddressAssistant;

    @Column(name = "post_code",nullable = false,length = 10)
    private String postCode;

    private String trackingNumber;

    private Delivery(Member member, OrderItem orderItem, DeliveryStatus deliveryStatus, String deliveryRequest, String deliveryAddress, String deliveryAddressAssistant,String postCode) {
        this.member = member;
        this.orderItem = orderItem;
        this.deliveryStatus = deliveryStatus;
        this.deliveryRequest = deliveryRequest;
        this.deliveryAddress = deliveryAddress;
        this.deliveryAddressAssistant = deliveryAddressAssistant;
        this.postCode = postCode;
    }

    public static Delivery of(Member member, OrderItem orderItem, String deliveryRequest, String deliveryAddress, String deliveryAddressAssistant,String postCode) {
        return new Delivery(member, orderItem, DeliveryStatus.READY,deliveryRequest, deliveryAddress, deliveryAddressAssistant,postCode);
    }


    public void update(String deliveryRequest, String deliveryAddress, String detailAddress,String postCode) {
        this.deliveryRequest = deliveryRequest;
        this.deliveryAddress = deliveryAddress;
        this.deliveryAddressAssistant = detailAddress;
        this.postCode = postCode;
    }

    public void changeStatus(DeliveryStatus deliveryStatus) {
        this.deliveryStatus = deliveryStatus;
    }

    public void insertTrackingNumber(String trackingNumber) {
        this.trackingNumber = trackingNumber;
    }
}
