package com.example.evostyle.domain.delivery.service;

import com.example.evostyle.domain.delivery.dto.request.DeliveryRequest;
import com.example.evostyle.domain.delivery.dto.response.DeliveryResponse;
import com.example.evostyle.domain.delivery.entity.Delivery;
import com.example.evostyle.domain.delivery.repository.DeliveryRepository;
import com.example.evostyle.domain.member.entity.Address;
import com.example.evostyle.domain.member.entity.Member;
import com.example.evostyle.domain.member.repository.AddressRepository;
import com.example.evostyle.domain.member.repository.MemberRepository;
import com.example.evostyle.domain.orderitem.entity.OrderItem;
import com.example.evostyle.domain.orderitem.repository.OrderItemsRepository;
import com.example.evostyle.global.exception.ErrorCode;
import com.example.evostyle.global.exception.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class DeliveryService {

    private final DeliveryRepository deliveryRepository;
    private final AddressRepository addressRepository;
    private final OrderItemsRepository orderItemsRepository;
    private final MemberRepository memberRepository;

    public DeliveryResponse createDelivery(Long addressId, Long orderItemId, Long memberId, DeliveryRequest deliveryRequest) {
        Address address = addressRepository.findById(addressId).orElseThrow(() -> new NotFoundException(ErrorCode.ADDRESS_NOT_FOUND));
        OrderItem orderItem = orderItemsRepository.findById(orderItemId).orElseThrow(() -> new NotFoundException(ErrorCode.ORDER_NOT_FOUND));
        Member member = memberRepository.findById(memberId).orElseThrow(() -> new NotFoundException(ErrorCode.MEMBER_NOT_FOUND));

        Delivery delivery = Delivery.of(member, orderItem, deliveryRequest.deliveryRequest(), address.getSiDo(), address.getDetailAddress());
        Delivery savedDelivery = deliveryRepository.save(delivery);
        return DeliveryResponse.from(savedDelivery);
    }


    public List<DeliveryResponse> getAllDeliveryByMember(Long memberId) {
        return null;
    }

    public DeliveryResponse updateDelivery(DeliveryRequest deliveryRequest) {
        return null;
    }

    public void deleteDelivery(Long deliveryId) {

    }
}
