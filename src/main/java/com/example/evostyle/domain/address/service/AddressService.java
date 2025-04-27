package com.example.evostyle.domain.address.service;

import com.example.evostyle.domain.address.dto.request.CreateAddressRequest;
import com.example.evostyle.domain.address.dto.response.CreateAddressResponse;
import com.example.evostyle.domain.address.dto.response.ReadAddressResponse;
import com.example.evostyle.domain.address.entity.Address;
import com.example.evostyle.domain.address.repository.AddressRepository;
import com.example.evostyle.domain.member.entity.Member;
import com.example.evostyle.domain.member.repository.MemberRepository;
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

        Address address = Address.of(
            request.postCode(), request.siDo(),
            request.siGunGu(), request.roadNameAddress(),
            request.detailAddress(), request.extraAddress(), member);

        addressRepository.save(address);

        return CreateAddressResponse.from(address);
    }

    public List<ReadAddressResponse> readAllAddresses(Long memberId) {
        List<Address> addressList = addressRepository.findByMemberId(memberId);

        return addressList.stream()
            .map(ReadAddressResponse::from)
            .toList();
    }
}
