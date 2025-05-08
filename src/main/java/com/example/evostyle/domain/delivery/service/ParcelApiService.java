package com.example.evostyle.domain.delivery.service;

import com.example.evostyle.domain.delivery.dto.DeliveryUserEvent;
import com.example.evostyle.domain.delivery.dto.request.ParcelRequest;
import com.example.evostyle.domain.delivery.dto.request.ParcelUpdateUserRequest;
import com.example.evostyle.domain.delivery.dto.request.ReceiverRequest;
import com.example.evostyle.domain.delivery.dto.request.SenderRequest;
import com.example.evostyle.domain.delivery.dto.response.ParcelResponse;
import com.example.evostyle.domain.delivery.entity.Delivery;
import com.example.evostyle.domain.member.entity.Address;
import com.example.evostyle.global.exception.ErrorCode;
import com.example.evostyle.global.exception.ExternalApiException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Slf4j
@Service
@RequiredArgsConstructor
public class ParcelApiService {

    private final WebClient webClient;

    @Value("${delivery.api.uri}")
    private String apiUri;


    public boolean isCorrectionFailed(Delivery delivery, Address address, DeliveryUserEvent deliveryUserEvent) {
        ParcelUpdateUserRequest parcelUpdateUserRequest = ParcelUpdateUserRequest.of(address, deliveryUserEvent.newDeliveryRequest());
        try {
            webClient.patch()
                    .uri(apiUri + delivery.getTrackingNumber())
                    .bodyValue(parcelUpdateUserRequest)
                    .retrieve()
                    .onStatus(status -> status.is4xxClientError() || status.is5xxServerError(), response -> {
                        return response.bodyToMono(String.class)
                                .flatMap(errorBody -> Mono.error(new ExternalApiException(ErrorCode.PARCEL_API_FAIL)));
                    })
                    .toBodilessEntity()
                    .block();
            return false;
        } catch (ExternalApiException e) {
            log.warn("사용자 요청 보정실패: {}", e.getMessage());
            return true;
        }
    }

    public ParcelResponse createTrackingNumber(Delivery delivery) {
        SenderRequest senderRequest = SenderRequest.of(delivery.getOrderItem().getBrand().getName());
        ReceiverRequest receiverRequest = ReceiverRequest.of(delivery.getMember().getNickname(), delivery.getDeliveryAddress(), delivery.getDeliveryAddressAssistant(), delivery.getMember().getPhoneNumber(), delivery.getPostCode());
        ParcelRequest parcelRequest = ParcelRequest.of(senderRequest, receiverRequest, delivery.getDeliveryRequest());

        ParcelResponse parcelResponse = webClient.post().uri(apiUri).
                bodyValue(parcelRequest).
                retrieve().onStatus(status -> status.is4xxClientError() || status.is5xxServerError(),response ->
                        response.bodyToMono(String.class)
                                .flatMap(msg -> Mono.error(new ExternalApiException(ErrorCode.PARCEL_API_FAIL))
                                )
                ).
                bodyToMono(ParcelResponse.class).
                block();
        return parcelResponse;
    }
}
