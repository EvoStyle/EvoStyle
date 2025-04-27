package com.example.evostyle.domain.address.repository;

import com.example.evostyle.domain.address.entity.Address;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AddressRepository extends JpaRepository<Address, Long> {

    List<Address> findByMemberId(Long memberId);
}
