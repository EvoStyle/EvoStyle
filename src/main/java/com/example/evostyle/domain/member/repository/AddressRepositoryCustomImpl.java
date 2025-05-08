package com.example.evostyle.domain.member.repository;

import com.example.evostyle.domain.member.entity.Address;
import com.example.evostyle.domain.member.entity.QAddress;
import com.example.evostyle.domain.member.entity.QMember;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AddressRepositoryCustomImpl implements AddressRepositoryCustom{

    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public Address findWithMemberById(Long addressId) {
        QAddress address = QAddress.address;

        return jpaQueryFactory.selectFrom(address)
                .leftJoin(address.member, QMember.member).fetchJoin()
                .where(address.id.eq(addressId))
                .fetchOne();
    }
}
