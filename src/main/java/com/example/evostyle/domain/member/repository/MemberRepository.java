package com.example.evostyle.domain.member.repository;

import com.example.evostyle.domain.member.entity.Member;
import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long> {

    @Query("SELECT m FROM Member m WHERE m.email = :email OR m.nickname = :nickname OR m.phoneNumber = :phoneNumber")
    List<Member> findDuplicates(@Param("email") String email,
                                @Param("nickname") String nickname,
                                @Param("phoneNumber") String phoneNumber);

    Optional<Member> findByEmail(String email);

    Optional<Member> findByIdAndIsDeletedFalse(Long id);
}
