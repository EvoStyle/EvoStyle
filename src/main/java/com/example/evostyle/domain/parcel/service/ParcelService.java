package com.example.evostyle.domain.parcel.service;

import com.example.evostyle.domain.parcel.dto.request.ParcelRequest;
import com.example.evostyle.domain.parcel.dto.response.ParcelResponse;
import com.example.evostyle.domain.parcel.dto.request.ReceiverRequest;
import com.example.evostyle.domain.parcel.entity.Parcel;
import com.example.evostyle.domain.parcel.entity.ParcelStatus;
import com.example.evostyle.domain.parcel.entity.Receiver;
import com.example.evostyle.domain.parcel.entity.Sender;
import com.example.evostyle.domain.parcel.exception.ParcelAlreadyReceivedException;
import com.example.evostyle.domain.parcel.repository.ParcelRepository;
import com.example.evostyle.domain.parcel.repository.ReceiverRepository;
import com.example.evostyle.domain.parcel.repository.SenderRepository;
import com.example.evostyle.global.exception.BadRequestException;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ParcelService {

    private final ParcelRepository parcelRepository;

    @Transactional
    public ParcelResponse createParcel(ParcelRequest parcelRequest) {
        Sender sender = Sender.of(parcelRequest.senderRequest().name());
        ReceiverRequest receiverRequest = parcelRequest.receiverRequest();
        Receiver receiver = Receiver.of(receiverRequest.name(), receiverRequest.address(), receiverRequest.phone(), receiverRequest.postCode(), parcelRequest.receiverRequest().addressAssistant());

        int retryCount = 0;

        final int MAX_RETRIES = 3;

        while (retryCount < MAX_RETRIES) {
            try {
                String trackingNumber = UUID.randomUUID().toString().replace("-", "").substring(0, 20).toUpperCase();
                Parcel parcel = Parcel.of(trackingNumber, sender, receiver, parcelRequest.deliveryRequest());
                Parcel savedParcel = parcelRepository.save(parcel);

                return ParcelResponse.from(savedParcel);

            } catch (DataIntegrityViolationException e) {
                retryCount++;
                if (retryCount >= MAX_RETRIES) {
                    throw new DataIntegrityViolationException("송장번호 생성 실패 – 여러 번 시도했으나 저장에 실패했습니다.");
                }
            }
        }
        throw new IllegalStateException("이곳에 도달하면 안됩니다.");
    }

    @Transactional
    public void deleteParcel(String parcelId) {
        Parcel parcel = parcelRepository.findById(parcelId).orElseThrow(() -> new IllegalArgumentException("송장번호가 존재하지 않습니다."));
        if (parcel.getParcelStatus() != ParcelStatus.ISSUED) {
            throw new ParcelAlreadyReceivedException("입고가 완료되어 출고할수 없습니다.");
        }
        parcelRepository.delete(parcel);
    }
}