package com.example.evostyle.domain.parcel.service;

import com.example.evostyle.domain.parcel.dto.request.ParcelRequest;
import com.example.evostyle.domain.parcel.dto.response.ParcelResponse;
import com.example.evostyle.domain.parcel.dto.request.ReceiverRequest;
import com.example.evostyle.domain.parcel.entity.Parcel;
import com.example.evostyle.domain.parcel.entity.Receiver;
import com.example.evostyle.domain.parcel.entity.Sender;
import com.example.evostyle.domain.parcel.repository.ParcelRepository;
import com.example.evostyle.domain.parcel.repository.ReceiverRepository;
import com.example.evostyle.domain.parcel.repository.SenderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ParcelService {

    private final ParcelRepository parcelRepository;
    private final SenderRepository senderRepository;
    private final ReceiverRepository receiverRepository;

    @Transactional
    public ParcelResponse createParcel(ParcelRequest parcelRequest) {
        String trackingNumber = UUID.randomUUID().toString().replace("-", "").substring(0, 20).toUpperCase();
        Sender sender = Sender.of(parcelRequest.senderRequest().name());
        ReceiverRequest receiverRequest = parcelRequest.receiverRequest();
        Receiver receiver = Receiver.of(receiverRequest.name(), receiverRequest.address(), receiverRequest.phone(), receiverRequest.postCode());
        Parcel parcel = Parcel.of(trackingNumber, sender, receiver);
        Parcel savedParcel = parcelRepository.save(parcel);
        return ParcelResponse.from(savedParcel);
    }
}
