package com.example.evostyle.domain.address.repository;

import com.example.evostyle.domain.address.entity.Address;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface AddressRepository extends JpaRepository<Address, Long> {

    Optional<Address> findByIdAndMemberId(Long Id, Long memberId);

    List<Address> findByMemberId(Long memberId);
}
