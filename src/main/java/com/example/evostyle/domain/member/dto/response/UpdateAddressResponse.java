package com.example.evostyle.domain.member.dto.response;

import com.example.evostyle.domain.member.entity.Address;

public record UpdateAddressResponse(Long id, Long memberId) {
    public static UpdateAddressResponse from(Address address) {
        return new UpdateAddressResponse(address.getId(), address.getMember().getId());
    }
}
