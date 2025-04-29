package com.example.evostyle.domain.delivery.service;

import com.example.evostyle.domain.delivery.dto.request.DeliveryRequest;
import com.example.evostyle.domain.delivery.dto.response.DeliveryResponse;
import com.example.evostyle.domain.delivery.repository.DeliveryRepository;
import com.example.evostyle.domain.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DeliveryService {

    private final DeliveryRepository deliveryRepository;
    private final MemberRepository memberRepository;
    private final AddressRepository addressRepository;
    private final OrderItemsRepository orderItemsRepository;

    public DeliveryResponse createDelivery(Long addressId, Long memberId, Long orderItemId) {

        return null;
    }
}
