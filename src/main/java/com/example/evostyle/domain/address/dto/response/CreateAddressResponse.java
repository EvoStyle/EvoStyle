package com.example.evostyle.domain.address.dto.response;

import com.example.evostyle.domain.address.entity.Address;

public record CreateAddressResponse(
    Long id,
    Long memberId
) {
    public static CreateAddressResponse from(Address address) {
        return new CreateAddressResponse(address.getId(), address.getMember().getId());
    }
}
