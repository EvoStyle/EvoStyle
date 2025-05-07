package com.example.evostyle.domain.member.entity;

import com.example.evostyle.common.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;

import java.time.LocalDateTime;

@Entity
@Getter
@Table(name = "members")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Member extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 50, unique = true, nullable = false)
    private String email;

    @Column(nullable = false)
    private String password;

    @Column(length = 50, unique = true, nullable = false)
    private String nickname;

    @Column(nullable = false)
    private Integer age;

    @Column(length = 15, name = "phone_number", unique = true, nullable = false)
    private String phoneNumber;

    @Enumerated(EnumType.STRING)
    @Column(length = 10, nullable = false)
    private Authority authority;

    @Enumerated(EnumType.STRING)
    @Column(length = 5, name = "gender_type", nullable = false)
    private GenderType genderType;

    @Column(name = "deleted_at")
    private LocalDateTime deletedAt;

    @Enumerated(EnumType.STRING)
    @Column(name = "member_gradle", nullable = false)
    private MemberGrade memberGrade = MemberGrade.MEMBER;

    @Column(name = "purchase_sum")
    @ColumnDefault("0")
    private Long purchaseSum = 0L;

    @Column(name = "is_deleted")
    @ColumnDefault("false")
    private boolean isDeleted = false;

    private Member(
            String email, String password, String nickname,
            Integer age, String phoneNumber, Authority authority, GenderType genderType
    ) {
        this.email = email;
        this.password = password;
        this.nickname = nickname;
        this.age = age;
        this.phoneNumber = phoneNumber;
        this.authority = authority;
        this.genderType = genderType;
    }

    public static Member of(
            String email, String password, String nickname,
            Integer age, String phoneNumber, Authority authority, GenderType genderType
    ) {
        return new Member(email, password, nickname, age, phoneNumber, authority, genderType);
    }

    public void updateMember(String nickname, Integer age, String phoneNumber) {
        this.nickname = nickname;
        this.age = age;
        this.phoneNumber = phoneNumber;
    }

    public void deleteMember() {
        this.isDeleted = true;
        this.deletedAt = LocalDateTime.now();
    }

    public void addToPurchaseSum(int amount) {
        this.purchaseSum += amount;
    }
    public void minusToPurchaseSum(int amount){this.purchaseSum -= amount;}

    public void promoteGrade() {
        MemberGrade[] values = MemberGrade.values();
        int nextOrdinal = this.memberGrade.ordinal() + 1;

        if (values[nextOrdinal].purchaseSum <= this.purchaseSum) {
            this.memberGrade = this.memberGrade.nextGrade();
        }
    }

}
