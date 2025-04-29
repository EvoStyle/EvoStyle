package com.example.evostyle.domain.delivery.entity;

import com.example.evostyle.domain.member.entity.Address;
import com.example.evostyle.domain.member.entity.Member;
import com.example.evostyle.domain.orderitem.entity.OrderItem;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Table(name = "delivery")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Delivery {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_items_id", nullable = false)
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

    private Delivery(Member member, OrderItem orderItem, DeliveryStatus deliveryStatus, String deliveryRequest, String deliveryAddress, String deliveryAddressAssistant) {
        this.member = member;
        this.orderItem = orderItem;
        this.deliveryStatus = deliveryStatus;
        this.deliveryRequest = deliveryRequest;
        this.deliveryAddress = deliveryAddress;
        this.deliveryAddressAssistant = deliveryAddressAssistant;
    }

    public static Delivery of(Member member, OrderItem orderItem, String deliveryRequest, String deliveryAddress, String deliveryAddressAssistant) {
        return new Delivery(member, orderItem, DeliveryStatus.READY,deliveryRequest, deliveryAddress, deliveryAddressAssistant);
    }


}
