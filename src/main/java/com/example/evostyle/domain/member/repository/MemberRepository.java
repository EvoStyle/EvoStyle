package com.example.evostyle.domain.member.repository;

import com.example.evostyle.domain.member.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long>, MemberQueryRepository {

    Optional<Member> findByEmail(String email);

    Optional<Member> findByIdAndIsDeletedFalse(Long id);
}
