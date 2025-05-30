package com.example.evostyle.domain.member.service;

import com.example.evostyle.domain.member.dto.request.CreateAddressRequest;
import com.example.evostyle.domain.member.dto.request.UpdateAddressRequest;
import com.example.evostyle.domain.member.dto.response.CreateAddressResponse;
import com.example.evostyle.domain.member.dto.response.ReadAddressResponse;
import com.example.evostyle.domain.member.dto.response.UpdateAddressResponse;
import com.example.evostyle.domain.member.entity.Address;
import com.example.evostyle.domain.member.repository.AddressRepository;
import com.example.evostyle.domain.member.entity.Member;
import com.example.evostyle.domain.member.repository.MemberRepository;
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
public class AddressService {

    private final AddressRepository addressRepository;
    private final MemberRepository memberRepository;

    @Transactional
    public CreateAddressResponse createAddress(Long memberId, CreateAddressRequest request) {
        Member member = memberRepository.findByIdAndIsDeletedFalse(memberId)
            .orElseThrow(() -> new NotFoundException(ErrorCode.MEMBER_NOT_FOUND));

        if (addressRepository.countByMemberId(memberId) >= 5) {
            throw new BadRequestException(ErrorCode.MAX_ADDRESS_LIMIT_EXCEEDED);
        }

        Address address = Address.of(
            request.postCode(), request.fullAddress(),
            request.detailAddress(), request.memo(), member);

        addressRepository.save(address);

        return CreateAddressResponse.from(address);
    }

    public List<ReadAddressResponse> readAllAddresses(Long memberId) {
        List<Address> addressList = addressRepository.findByMemberId(memberId);

        return addressList.stream()
            .map(ReadAddressResponse::from)
            .toList();
    }

    @Transactional
    public UpdateAddressResponse updateAddress(Long memberId, Long addressId, UpdateAddressRequest request) {
        Address address = addressRepository.findByIdAndMemberId(addressId, memberId)
            .orElseThrow(() -> new NotFoundException(ErrorCode.ADDRESS_NOT_FOUND));

        address.updateAddress(
            request.postCode(), request.fullAddress(), request.detailAddress(), request.memo());

        return UpdateAddressResponse.from(address);
    }

    @Transactional
    public UpdateAddressResponse updateIsBasecamp(Long memberId, Long addressId) {
        addressRepository.updateAllBasecampFalse(memberId);

        Address address = addressRepository.findByIdAndMemberId(addressId, memberId)
            .orElseThrow(() -> new NotFoundException(ErrorCode.ADDRESS_NOT_FOUND));

        address.updateIsBasecamp();

        return UpdateAddressResponse.from(address);
    }

    @Transactional
    public void deleteAddress(Long addressId, Long memberId) {
        Address address = addressRepository.findByIdAndMemberId(addressId, memberId)
            .orElseThrow(() -> new NotFoundException(ErrorCode.ADDRESS_NOT_FOUND));

        addressRepository.delete(address);
    }
}
