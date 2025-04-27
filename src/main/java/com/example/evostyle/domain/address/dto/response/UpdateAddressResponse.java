package com.example.evostyle.domain.address.dto.response;

import com.example.evostyle.domain.address.entity.Address;

public record UpdateAddressResponse(Long id, Long memberId) {
    public static UpdateAddressResponse from(Address address) {
        return new UpdateAddressResponse(address.getId(), address.getMember().getId());
    }
}
