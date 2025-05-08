package com.example.evostyle.domain.member.repository;

import com.example.evostyle.domain.member.entity.Address;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface AddressRepository extends JpaRepository<Address, Long>, AddressQueryRepository {

    Optional<Address> findByIdAndMemberId(Long Id, Long memberId);

    List<Address> findByMemberId(Long memberId);

    long countByMemberId(Long memberId);
}
