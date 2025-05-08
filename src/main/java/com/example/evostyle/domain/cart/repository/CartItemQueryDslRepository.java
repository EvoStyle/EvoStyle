package com.example.evostyle.domain.cart.repository;

import com.example.evostyle.domain.cart.entity.CartItem;

import java.util.List;

public interface CartItemQueryDslRepository {

    List<CartItem> findCartItemWithOptions(Long cartId);
}
