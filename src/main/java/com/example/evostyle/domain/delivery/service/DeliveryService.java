package com.example.evostyle.domain.delivery.service;

import com.example.evostyle.domain.delivery.dto.request.DeliveryRequest;
import com.example.evostyle.domain.delivery.dto.response.DeliveryResponse;
import com.example.evostyle.domain.delivery.entity.Delivery;
import com.example.evostyle.domain.delivery.entity.DeliveryStatus;
import com.example.evostyle.domain.delivery.repository.DeliveryRepository;
import com.example.evostyle.domain.member.entity.Address;
import com.example.evostyle.domain.member.entity.Member;
import com.example.evostyle.domain.member.repository.AddressRepository;
import com.example.evostyle.domain.member.repository.MemberRepository;
import com.example.evostyle.domain.order.entity.OrderItem;
import com.example.evostyle.domain.orderitem.repository.OrderItemsRepository;
import com.example.evostyle.global.exception.BadRequestException;
import com.example.evostyle.global.exception.ErrorCode;
import com.example.evostyle.global.exception.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class DeliveryService {

    private final DeliveryRepository deliveryRepository;
    private final AddressRepository addressRepository;
    private final OrderItemsRepository orderItemsRepository;
    private final MemberRepository memberRepository;

    @Transactional
    public DeliveryResponse createDelivery(Long addressId, Long orderItemId, Long memberId, DeliveryRequest deliveryRequest) {
        Address address = addressRepository.findById(addressId).orElseThrow(() -> new NotFoundException(ErrorCode.ADDRESS_NOT_FOUND));
        OrderItem orderItem = orderItemsRepository.findById(orderItemId).orElseThrow(() -> new NotFoundException(ErrorCode.ORDER_NOT_FOUND));
        Member member = memberRepository.findById(memberId).orElseThrow(() -> new NotFoundException(ErrorCode.MEMBER_NOT_FOUND));

        Delivery delivery = Delivery.of(member, orderItem, deliveryRequest.deliveryRequest(), address.getFullAddress(), address.getDetailAddress());
        Delivery savedDelivery = deliveryRepository.save(delivery);
        return DeliveryResponse.from(savedDelivery);
    }


    public List<DeliveryResponse> getAllDeliveryByMember(Long memberId) {
        List<Delivery> allByMemberId = deliveryRepository.findAllByMemberId(memberId);
        return allByMemberId.stream().map(DeliveryResponse::from).toList();
    }

    @Transactional
    public DeliveryResponse updateDelivery(DeliveryRequest deliveryRequest, Long addressId, Long deliveryId) {
        Delivery delivery = deliveryRepository.findById(deliveryId).orElseThrow(() -> new NotFoundException(ErrorCode.DELIVERY_NOT_FOUND));

        if (!delivery.getDeliveryStatus().equals(DeliveryStatus.READY)) {
            throw new BadRequestException(ErrorCode.DELIVERY_NOT_READY);
        }
        Address address = addressRepository.findById(addressId).orElseThrow(() -> new NotFoundException(ErrorCode.ADDRESS_NOT_FOUND));
        delivery.update(deliveryRequest.deliveryRequest(), address.getFullAddress(), address.getDetailAddress());
        Delivery savedDelivery = deliveryRepository.save(delivery);
        return DeliveryResponse.from(savedDelivery);
    }

    @Transactional
    public DeliveryResponse changeDeliveryStatusToShipped(Long deliveryId) {
        Delivery delivery = deliveryRepository.findById(deliveryId).orElseThrow(() -> new NotFoundException(ErrorCode.DELIVERY_NOT_FOUND));
        if (!delivery.getDeliveryStatus().equals(DeliveryStatus.READY)) {
            throw new BadRequestException(ErrorCode.DELIVERY_NOT_READY);
        }
        delivery.changeStatus(DeliveryStatus.SHIPPED);
        Delivery savedDelivery = deliveryRepository.save(delivery);
        return DeliveryResponse.from(savedDelivery);
    }

    @Transactional
    public void deleteDelivery(Long deliveryId) {
        Delivery delivery = deliveryRepository.findById(deliveryId).orElseThrow(() -> new NotFoundException(ErrorCode.DELIVERY_NOT_FOUND));
        delivery.changeStatus(DeliveryStatus.CANCELLED);
    }
}
