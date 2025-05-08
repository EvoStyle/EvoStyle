package com.example.evostyle.domain.cart.entity;

import com.example.evostyle.common.entity.BaseEntity;
import com.example.evostyle.domain.member.entity.Member;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;

import java.io.Serializable;
import java.util.List;

@Entity
@Getter
@Table(name = "carts")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Cart extends BaseEntity {

    @Id 
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    private Member member;

    @ColumnDefault("0")
    private Integer totalPrice = 0;

    @ColumnDefault("0")
    private Integer discountPrice = 0;

    private Cart(Member member){
        this.member = member;

    }

    public static Cart of(Member member){
        return new Cart(member);
    }
}
