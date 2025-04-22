package com.example.evostyle.domain.cart.entity;

import com.example.evostyle.common.entity.BaseEntity;
import com.example.evostyle.domain.cart.cartitem.CartItem;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;

import java.util.List;

@Entity
@Getter
@Table(name = "carts")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Cart extends BaseEntity {

    @Id 
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_id")
    private Long userId;

    @ColumnDefault("0")
    private Integer totalPrice = 0;

    private Cart(Long userId){
        this.userId = userId;

    }

    public static Cart of(Long userId, List<CartItem> cartItemList){
        return new Cart(userId);
    }
}
