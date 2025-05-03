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

    private String addressAssistant;

    private String phone;

    private String postCode;

    private Receiver(String name, String address, String phone, String postCode, String addressAssistant) {
        this.name = name;
        this.address = address;
        this.phone = phone;
        this.postCode = postCode;
        this.addressAssistant = addressAssistant;
    }

    public static Receiver of(String name, String address, String phone, String postCode,String addressAssistant) {
        return new Receiver(name, address, phone, postCode,addressAssistant);
    }

    public void update(String address, String addressAssistant, String postCode) {
        this.address = address;
        this.addressAssistant = addressAssistant;
        this.postCode = postCode;
    }
}
