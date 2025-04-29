package com.example.evostyle.domain.member.repository;

import com.example.evostyle.domain.member.entity.Address;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AddressRepository extends JpaRepository<Address, Long> {
}
