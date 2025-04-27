package com.example.evostyle.domain.address.repository;

import com.example.evostyle.domain.address.entity.Address;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AddressRepository extends JpaRepository<Address, Long> {
}
