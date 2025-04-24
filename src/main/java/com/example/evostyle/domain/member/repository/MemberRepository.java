package com.example.evostyle.domain.member.repository;

import com.example.evostyle.domain.member.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MemberRepository extends JpaRepository<Member, Long> {

    boolean existsByEmail (String email);
}
