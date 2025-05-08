package com.example.evostyle.domain.delivery.service;

import com.example.evostyle.common.util.JsonHelper;
import com.example.evostyle.domain.delivery.dto.*;
import com.example.evostyle.domain.delivery.dto.request.*;
import com.example.evostyle.domain.delivery.dto.response.DeliveryResponse;
import com.example.evostyle.domain.delivery.dto.response.DeliveryResponseForBrand;
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
import jakarta.persistence.EntityManager;
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
    private final EntityManager entityManager;


    @Transactional
    public DeliveryResponse createDelivery(Long addressId, Long orderItemId, Long memberId, DeliveryRequest deliveryRequest) {
        Address address = addressRepository.findWithMemberById(addressId);
        OrderItem orderItem = orderItemRepository.findById(orderItemId).orElseThrow(() -> new NotFoundException(ErrorCode.ORDER_NOT_FOUND));

        Delivery delivery = Delivery.of(address.getMember(), orderItem,orderItem.getBrand() ,deliveryRequest.deliveryRequest(), address.getFullAddress(), address.getDetailAddress(), address.getPostCode());
        Delivery savedDelivery = deliveryRepository.save(delivery);
        return DeliveryResponse.from(savedDelivery);
    }

    @Transactional(readOnly = true)
    public List<DeliveryResponse> getAllDeliveryByMember(Long memberId) {
        List<Delivery> allByMemberId = deliveryRepository.findAllWithMemberByMemberId(memberId);
        return allByMemberId.stream().map(DeliveryResponse::from).toList();
    }


    @Transactional
    public Delivery updateDelivery(DeliveryUserEvent deliveryUserEvent, Delivery delivery, Address address) {
        delivery.update(deliveryUserEvent.newDeliveryRequest(), address.getFullAddress(), address.getDetailAddress(), address.getPostCode());
        return deliveryRepository.save(delivery);
    }


    @Transactional
    public AdminDeliveryResponse performShipping(ParcelResponse parcelResponse, Delivery delivery) {
        delivery.changeStatus(DeliveryStatus.SHIPPED);

        delivery.insertTrackingNumber(parcelResponse.trackingNumber());

        deliveryRepository.save(delivery);

        return AdminDeliveryResponse.from(delivery);
    }

    private void cancelParcelApi(String trackingNumber) {

    }

    @Transactional
    public void deleteDelivery(Long deliveryId) {
        Delivery delivery = deliveryRepository.findById(deliveryId).orElseThrow(() -> new NotFoundException(ErrorCode.DELIVERY_NOT_FOUND));
        delivery.changeStatus(DeliveryStatus.CANCELLED);
    }

    public List<DeliveryResponseForBrand> getAllDeliveryByBrand(Long brandId) {
        List<Delivery> deliveryList = deliveryRepository.findAllWithOrderItemAndMemberByBrandId(brandId);
        return deliveryList.stream().map(DeliveryResponseForBrand::from).toList();
    }
}
