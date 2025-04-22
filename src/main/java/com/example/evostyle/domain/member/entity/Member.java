package com.example.evostyle.domain.member.entity;

import com.example.evostyle.common.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Table(name = "members")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Member extends BaseEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 50, unique = true, nullable = false)
    private String email;

    @Column(nullable = false)
    private String password;

    @Column(length = 50, nullable = false)
    private String name;

    @Column(nullable = false)
    private Integer age;

    @Column(length = 15, name = "phone_number", nullable = false)
    private String phoneNumber;

    @Enumerated(EnumType.STRING)
    @Column(length = 10, nullable = false)
    private Authority authority;

    @Enumerated(EnumType.STRING)
    @Column(length = 5, name = "gender_type", nullable = false)
    private GenderType genderType;

    private Member(
        String email, String password, String name,
        Integer age, String phoneNumber, Authority authority, GenderType genderType
    ) {
        this.email = email;
        this.password = password;
        this.name = name;
        this.age = age;
        this.phoneNumber = phoneNumber;
        this.authority = authority;
        this.genderType = genderType;
    }

    public static Member of(
        String email, String password, String name,
        Integer age, String phoneNumber, Authority authority, GenderType genderType
    ) {
        return new Member(email, password, name, age, phoneNumber, authority, genderType);
    }
}
