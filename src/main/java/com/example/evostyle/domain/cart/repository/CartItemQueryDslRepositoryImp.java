package com.example.evostyle.domain.cart.repository;


import com.example.evostyle.domain.cart.entity.CartItem;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

import static com.example.evostyle.domain.cart.entity.QCart.cart;
import static com.example.evostyle.domain.cart.entity.QCartItem.cartItem;
import static com.example.evostyle.domain.product.optiongroup.entity.QOption.option;
import static com.example.evostyle.domain.product.productdetail.entity.QProductDetail.productDetail;
import static com.example.evostyle.domain.product.productdetail.entity.QProductDetailOption.productDetailOption;

@Repository
@RequiredArgsConstructor
public class CartItemQueryDslRepositoryImp implements CartItemQueryDslRepository{


    private final JPAQueryFactory queryFactory;
    @Override
    public List<CartItem> findCartItemWithOptions(Long cartId) {
        List<CartItem> results = queryFactory
                .selectDistinct(cartItem)
                .from(cartItem)
                .join(cartItem.cart, cart).fetchJoin()
                .join(cartItem.productDetail, productDetail).fetchJoin()
                .join(productDetailOption).on(productDetailOption.productDetail.id.eq(productDetail.id)).fetchJoin()
                .join(productDetailOption.option, option)
                .where(cart.id.eq(cartId))
                .fetch();

        return results;

    }
}
