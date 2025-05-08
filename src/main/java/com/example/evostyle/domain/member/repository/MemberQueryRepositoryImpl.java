package com.example.evostyle.domain.member.repository;

import com.example.evostyle.domain.member.entity.Member;
import com.example.evostyle.domain.member.entity.QMember;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class MemberQueryRepositoryImpl implements MemberQueryRepository{

    private final JPAQueryFactory queryFactory;

    @Override
    public List<Member> findDuplicates(String email, String nickname, String phoneNumber) {
        QMember member = QMember.member;

        return queryFactory
            .selectFrom(member)
            .where(
                member.email.eq(email)
                    .or(member.nickname.eq(nickname))
                    .or(member.phoneNumber.eq(phoneNumber))
            )
            .fetch();
    }
}
