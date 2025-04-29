package com.example.evostyle.domain.delivery.entity;

import com.example.evostyle.domain.address.entity.Address;
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
    @JoinColumn(name = "address_id", nullable = false)
    private Address address;

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

    private Delivery(Address address, OrderItem orderItem, DeliveryStatus deliveryStatus, String deliveryRequest, String deliveryAddress, String deliveryAddressAssistant) {
        this.address = address;
        this.orderItem = orderItem;
        this.deliveryStatus = deliveryStatus;
        this.deliveryRequest = deliveryRequest;
        this.deliveryAddress = deliveryAddress;
        this.deliveryAddressAssistant = deliveryAddressAssistant;
    }

    public static Delivery of(Address address, OrderItem orderItem, String deliveryRequest, String deliveryAddress, String deliveryAddressAssistant) {
        return new Delivery(address, orderItem, DeliveryStatus.READY,deliveryRequest, deliveryAddress, deliveryAddressAssistant);
    }


}
