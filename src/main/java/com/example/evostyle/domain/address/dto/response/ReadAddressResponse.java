package com.example.evostyle.domain.address.dto.response;

import com.example.evostyle.domain.address.entity.Address;

public record ReadAddressResponse(Long id, Long memberId) {
    public static ReadAddressResponse from(Address address) {
        return new ReadAddressResponse(address.getId(), address.getMember().getId());
    }
}
