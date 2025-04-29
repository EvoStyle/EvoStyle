package com.example.evostyle.domain.member.dto.response;

import com.example.evostyle.domain.member.entity.Address;

public record CreateAddressResponse(
    Long id,
    Long memberId
) {
    public static CreateAddressResponse from(Address address) {
        return new CreateAddressResponse(address.getId(), address.getMember().getId());
    }
}
