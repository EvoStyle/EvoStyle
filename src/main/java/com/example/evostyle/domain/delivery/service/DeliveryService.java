package com.example.evostyle.domain.delivery.service;

import com.example.evostyle.common.util.JsonHelper;
import com.example.evostyle.domain.delivery.dto.*;
import com.example.evostyle.domain.delivery.dto.request.*;
import com.example.evostyle.domain.delivery.dto.response.DeliveryResponse;
import com.example.evostyle.domain.delivery.dto.response.ParcelResponse;
import com.example.evostyle.domain.delivery.entity.Delivery;
import com.example.evostyle.domain.delivery.entity.DeliveryStatus;
import com.example.evostyle.domain.delivery.repository.DeliveryRepository;
import com.example.evostyle.domain.member.entity.Address;
import com.example.evostyle.domain.member.entity.Member;
import com.example.evostyle.domain.member.repository.AddressRepository;
import com.example.evostyle.domain.member.repository.MemberRepository;
import com.example.evostyle.domain.order.entity.OrderItem;
import com.example.evostyle.domain.order.repository.OrderItemRepository;
import com.example.evostyle.global.exception.*;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class DeliveryService {

    private final DeliveryRepository deliveryRepository;
    private final AddressRepository addressRepository;
    private final OrderItemRepository orderItemRepository;
    private final MemberRepository memberRepository;
    private final KafkaTemplate<String, String> kafkaTemplate;
    private final ParcelApiService parcelApiService;
    private final JsonHelper jsonHelper;


    @Transactional
    public DeliveryResponse createDelivery(Long addressId, Long orderItemId, Long memberId, DeliveryRequest deliveryRequest) {
        Address address = addressRepository.findById(addressId).orElseThrow(() -> new NotFoundException(ErrorCode.ADDRESS_NOT_FOUND));
        OrderItem orderItem = orderItemRepository.findById(orderItemId).orElseThrow(() -> new NotFoundException(ErrorCode.ORDER_NOT_FOUND));
        Member member = memberRepository.findById(memberId).orElseThrow(() -> new NotFoundException(ErrorCode.MEMBER_NOT_FOUND));

        Delivery delivery = Delivery.of(member, orderItem,orderItem.getBrand() ,deliveryRequest.deliveryRequest(), address.getFullAddress(), address.getDetailAddress(), address.getPostCode());
        Delivery savedDelivery = deliveryRepository.save(delivery);
        return DeliveryResponse.from(savedDelivery);
    }

    @Transactional(readOnly = true)
    public List<DeliveryResponse> getAllDeliveryByMember(Long memberId) {
        List<Delivery> allByMemberId = deliveryRepository.findAllByMemberId(memberId);
        return allByMemberId.stream().map(DeliveryResponse::from).toList();
    }


    public String updateDelivery(DeliveryUserEvent deliveryUserEvent) {
        Delivery delivery = deliveryRepository.findById(deliveryUserEvent.deliveryId()).orElseThrow(() -> new NotFoundException(ErrorCode.DELIVERY_NOT_FOUND));
        Address address = addressRepository.findById(deliveryUserEvent.addressId()).orElseThrow(() -> new NotFoundException(ErrorCode.ADDRESS_NOT_FOUND));

        if (!delivery.getDeliveryStatus().equals(DeliveryStatus.READY)) {
            if (parcelApiService.isCorrectionFailed(delivery, address, deliveryUserEvent)) {
                UserNotificationEvent fail = UserNotificationEvent.fail(deliveryUserEvent.userId(), delivery.getTrackingNumber());
                String payload = jsonHelper.toJson(fail);
                kafkaTemplate.send("user-notification-topic", deliveryUserEvent.userId().toString(), payload);
            }
        }
        Delivery savedDelivery = updateDelivery(deliveryUserEvent, delivery, address);
        return savedDelivery.getTrackingNumber();
    }


    @Transactional
    private Delivery updateDelivery(DeliveryUserEvent deliveryUserEvent, Delivery delivery, Address address) {
        delivery.update(deliveryUserEvent.newDeliveryRequest(), address.getFullAddress(), address.getDetailAddress(), address.getPostCode());
        return deliveryRepository.save(delivery);
    }


    public AdminDeliveryResponse changeDeliveryStatusToShipped(DeliveryAdminEvent deliveryAdminEvent) {

        Delivery delivery = deliveryRepository.findById(deliveryAdminEvent.deliveryId()).orElseThrow(() -> new NotFoundException(ErrorCode.DELIVERY_NOT_FOUND));
        if (delivery.getDeliveryStatus() != DeliveryStatus.READY) {
            if (delivery.getTrackingNumber().isEmpty()) {
                //log 찍고 알림을 보내는게 좋을듯 여기도달하면 단단히 잘못된것임
            }
            AdminNotificationEvent fail = AdminNotificationEvent.fail(delivery);
            String payload = jsonHelper.toJson(fail);
            kafkaTemplate.send("admin-notification-topic",delivery.getId().toString(),payload);
        }
        ParcelResponse parcelResponse = parcelApiService.createTrackingNumber(delivery);
        return performShipping(parcelResponse, delivery);
    }

    @Transactional
    private AdminDeliveryResponse performShipping(ParcelResponse parcelResponse, Delivery delivery) {

        delivery.changeStatus(DeliveryStatus.SHIPPED);

        delivery.insertTrackingNumber(parcelResponse.trackingNumber());

        Delivery savedDelivery = deliveryRepository.save(delivery);

        return AdminDeliveryResponse.from(savedDelivery);

    }



    private void cancelParcelApi(String trackingNumber) {

    }

    @Transactional
    public void deleteDelivery(Long deliveryId) {
        Delivery delivery = deliveryRepository.findById(deliveryId).orElseThrow(() -> new NotFoundException(ErrorCode.DELIVERY_NOT_FOUND));
        delivery.changeStatus(DeliveryStatus.CANCELLED);
    }
}
