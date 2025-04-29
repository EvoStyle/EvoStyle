package com.example.evostyle.domain.parcel.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Table(name = "receiver")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Receiver {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private String address;

    private String phone;

    private String postCode;

    private Receiver(String name, String address, String phone, String postCode) {
        this.name = name;
        this.address = address;
        this.phone = phone;
        this.postCode = postCode;
    }

    public static Receiver of(String name, String address, String phone, String postCode) {
        return new Receiver(name, address, phone, postCode);
    }
}
