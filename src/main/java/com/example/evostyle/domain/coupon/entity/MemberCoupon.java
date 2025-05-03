package com.example.evostyle.domain.coupon.entity;

import com.example.evostyle.common.entity.BaseEntity;
import com.example.evostyle.domain.member.entity.Member;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;

@Entity
@Getter
@Table(name = "member_coupons")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MemberCoupon extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "is_used")
    @ColumnDefault("false")
    private Boolean isUsed;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "coupon_id", nullable = false)
    private Coupon coupon;

    private MemberCoupon(Boolean isUsed, Member member, Coupon coupon) {
        this.isUsed = isUsed;
        this.member = member;
        this.coupon = coupon;
    }

    public static MemberCoupon of(Boolean isUsed, Member member, Coupon coupon) {
        return new MemberCoupon(isUsed, member, coupon);
    }
}
