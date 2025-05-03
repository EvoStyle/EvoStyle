package com.example.evostyle.domain.delivery.service;

import com.example.evostyle.domain.delivery.dto.DeliveryAdminEvent;
import com.example.evostyle.domain.delivery.dto.DeliveryUserEvent;
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
import com.example.evostyle.domain.orderitem.repository.OrderItemsRepository;
import com.example.evostyle.global.exception.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class DeliveryService {

    private final DeliveryRepository deliveryRepository;
    private final AddressRepository addressRepository;
    private final OrderItemsRepository orderItemsRepository;
    private final MemberRepository memberRepository;

    private final ParcelApiService parcelApiService;


    @Transactional
    public DeliveryResponse createDelivery(Long addressId, Long orderItemId, Long memberId, DeliveryRequest deliveryRequest) {
        Address address = addressRepository.findById(addressId).orElseThrow(() -> new NotFoundException(ErrorCode.ADDRESS_NOT_FOUND));
        OrderItem orderItem = orderItemsRepository.findById(orderItemId).orElseThrow(() -> new NotFoundException(ErrorCode.ORDER_NOT_FOUND));
        Member member = memberRepository.findById(memberId).orElseThrow(() -> new NotFoundException(ErrorCode.MEMBER_NOT_FOUND));

        Delivery delivery = Delivery.of(member, orderItem, deliveryRequest.deliveryRequest(), address.getFullAddress(), address.getDetailAddress(), address.getPostCode());
        Delivery savedDelivery = deliveryRepository.save(delivery);
        return DeliveryResponse.from(savedDelivery);
    }

    @Transactional(readOnly = true)
    public List<DeliveryResponse> getAllDeliveryByMember(Long memberId) {
        List<Delivery> allByMemberId = deliveryRepository.findAllByMemberId(memberId);
        return allByMemberId.stream().map(DeliveryResponse::from).toList();
    }


    public DeliveryResponse updateDelivery(DeliveryUserEvent deliveryUserEvent) {
        Delivery delivery = deliveryRepository.findById(deliveryUserEvent.deliveryId()).orElseThrow(() -> new NotFoundException(ErrorCode.DELIVERY_NOT_FOUND));
        Address address = addressRepository.findById(deliveryUserEvent.addressId()).orElseThrow(() -> new NotFoundException(ErrorCode.ADDRESS_NOT_FOUND));

        if (!delivery.getDeliveryStatus().equals(DeliveryStatus.READY)) {
            if (parcelApiService.isCorrectionFailed(delivery, address, deliveryUserEvent)) {
                throw new BadRequestException(ErrorCode.DELIVERY_NOT_READY);
            }
        }
        Delivery savedDelivery = updateDelivery(deliveryUserEvent, delivery, address);
        return DeliveryResponse.from(savedDelivery);
    }


    @Transactional
    private Delivery updateDelivery(DeliveryUserEvent deliveryUserEvent, Delivery delivery, Address address) {
        delivery.update(deliveryUserEvent.newDeliveryRequest(), address.getFullAddress(), address.getDetailAddress(), address.getPostCode());
        return deliveryRepository.save(delivery);
    }


    public DeliveryResponse changeDeliveryStatusToShipped(DeliveryAdminEvent deliveryAdminEvent) {

        Delivery delivery = deliveryRepository.findById(deliveryAdminEvent.deliveryId()).orElseThrow(() -> new NotFoundException(ErrorCode.DELIVERY_NOT_FOUND));
        if (delivery.getDeliveryStatus() != DeliveryStatus.READY) {
            throw new ConflictException(ErrorCode.DELIVERY_CONFLICT_MODIFIED_BY_ADMIN);
        }
        ParcelResponse parcelResponse = parcelApiService.createTrackingNumber(delivery);
        return performShipping(parcelResponse, delivery);
    }

    @Transactional
    private DeliveryResponse performShipping( ParcelResponse parcelResponse, Delivery delivery) {

        delivery.changeStatus(DeliveryStatus.SHIPPED);

        delivery.insertTrackingNumber(parcelResponse.trackingNumber());

        Delivery savedDelivery = deliveryRepository.save(delivery);

        return DeliveryResponse.from(savedDelivery);

    }



    private void cancelParcelApi(String trackingNumber) {

    }

    @Transactional
    public void deleteDelivery(Long deliveryId) {
        Delivery delivery = deliveryRepository.findById(deliveryId).orElseThrow(() -> new NotFoundException(ErrorCode.DELIVERY_NOT_FOUND));
        delivery.changeStatus(DeliveryStatus.CANCELLED);
    }
}
