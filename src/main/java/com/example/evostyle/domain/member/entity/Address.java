package com.example.evostyle.domain.member.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;

@Entity
@Getter
@Table(name = "addresses")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Address {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 10, name = "post_code", nullable = false)
    private String postCode;

    @Column(length = 100, name = "full_address", nullable = false)
    private String fullAddress;

    @Column(length = 50, name = "detail_address", nullable = false)
    private String detailAddress;

    @Column(length = 100)
    private String memo;

    @Column(name = "is_basecamp", nullable = false)
    @ColumnDefault("false")
    private boolean isBasecamp;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;

    private Address(
        String postCode, String fullAddress, String detailAddress, String memo, Member member
    ) {
        this.postCode = postCode;
        this.fullAddress = fullAddress;
        this.detailAddress = detailAddress;
        this.memo = memo;
        this.member = member;
    }

    public static Address of(
        String postCode, String fullAddress, String detailAddress, String memo, Member member
    ) {
        return new Address(postCode, fullAddress, detailAddress, memo, member);
    }

    public void updateAddress(
        String postCode, String fullAddress, String detailAddress, String memo
    ) {
        if (postCode != null && !postCode.isBlank()) {
            this.postCode = postCode;
        }
        if (fullAddress != null && !fullAddress.isBlank()) {
            this.fullAddress = fullAddress;
        }
        if (detailAddress != null && !detailAddress.isBlank()) {
            this.detailAddress = detailAddress;
        }
        if (memo != null && !memo.isBlank()) {
            this.memo = memo;
        }
    }

    public void updateIsBasecamp() {
        this.isBasecamp = true;
    }
}
