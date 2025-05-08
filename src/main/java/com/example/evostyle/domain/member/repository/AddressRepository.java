package com.example.evostyle.domain.member.repository;

import com.example.evostyle.domain.member.entity.Address;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface AddressRepository extends JpaRepository<Address, Long> ,AddressRepositoryCustom{

    Optional<Address> findByIdAndMemberId(Long Id, Long memberId);

    List<Address> findByMemberId(Long memberId);

    @Modifying
    @Query("UPDATE Address a SET a.isBasecamp = false WHERE a.member.id = :memberId")
    void updateAllBasecampFalse(Long memberId);

    long countByMemberId(Long memberId);
}
