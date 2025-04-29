package com.example.evostyle.domain.member.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

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

    @Column(length = 15, name = "si_do", nullable = false)
    private String siDo;

    @Column(length = 30, name = "si_gun_gu", nullable = false)
    private String siGunGu;

    @Column(length = 50, name = "road_name_address", nullable = false)
    private String roadNameAddress;

    @Column(length = 50, name = "detail_address", nullable = false)
    private String detailAddress;

    @Column(length = 50, name = "extra_address")
    private String extraAddress;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;

    private Address(
        String postCode, String siDo, String siGunGu,
        String roadNameAddress, String detailAddress, String extraAddress, Member member
    ) {
        this.postCode = postCode;
        this.siDo = siDo;
        this.siGunGu = siGunGu;
        this.roadNameAddress = roadNameAddress;
        this.detailAddress = detailAddress;
        this.extraAddress = extraAddress;
        this.member = member;
    }

    public static Address of(
        String postCode, String siDo, String siGunGu,
        String roadNameAddress, String detailAddress, String extraAddress, Member member
    ) {
        return new Address(postCode, siDo, siGunGu, roadNameAddress, detailAddress, extraAddress, member);
    }

    public void update(
        String postCode, String siDo, String siGunGu,
        String roadNameAddress, String detailAddress, String extraAddress
    ) {
        if (postCode != null && !postCode.isBlank()) {
            this.postCode = postCode;
        }
        if (siDo != null && !siDo.isBlank()) {
            this.siDo = siDo;
        }
        if (siGunGu != null && !siGunGu.isBlank()) {
            this.siGunGu = siGunGu;
        }
        if (roadNameAddress != null && !roadNameAddress.isBlank()) {
            this.roadNameAddress = roadNameAddress;
        }
        if (detailAddress != null && !detailAddress.isBlank()) {
            this.detailAddress = detailAddress;
        }
        if (extraAddress != null && !extraAddress.isBlank()) {
            this.extraAddress = extraAddress;
        }
    }
}
