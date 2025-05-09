package com.example.evostyle.domain.member.repository;

import com.example.evostyle.domain.member.entity.Address;

public interface AddressRepositoryCustom {
    Address findWithMemberById(Long addressId);
}
