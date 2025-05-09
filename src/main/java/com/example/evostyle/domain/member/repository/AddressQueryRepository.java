package com.example.evostyle.domain.member.repository;

import com.example.evostyle.domain.member.entity.Address;

public interface AddressQueryRepository {
    void updateAllBasecampFalse(Long memberId);

    Address findWithMemberById(Long addressId);
}
