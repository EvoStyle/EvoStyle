package com.example.evostyle.domain.member.repository;

import com.example.evostyle.domain.member.entity.Address;
import com.example.evostyle.domain.member.entity.QAddress;
import com.example.evostyle.domain.member.entity.QMember;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class AddressQueryRepositoryImpl implements AddressQueryRepository{

    private final JPAQueryFactory queryFactory;

    @Override
    public void updateAllBasecampFalse(Long memberId) {
        QAddress address = QAddress.address;

        queryFactory
            .update(address)
            .set(address.isBasecamp, false)
            .where(address.member.id.eq(memberId))
            .execute();
    }

    @Override
    public Address findWithMemberById(Long addressId) {
        QAddress address = QAddress.address;

        return queryFactory.selectFrom(address)
                .leftJoin(address.member, QMember.member).fetchJoin()
                .where(address.id.eq(addressId))
                .fetchOne();
    }
}
