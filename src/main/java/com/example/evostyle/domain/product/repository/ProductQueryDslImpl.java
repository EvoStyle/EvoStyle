package com.example.evostyle.domain.product.repository;

import com.example.evostyle.domain.product.entity.QProduct;
import com.example.evostyle.domain.product.optiongroup.entity.QOption;
import com.example.evostyle.domain.product.optiongroup.entity.QOptionGroup;
import com.example.evostyle.domain.product.productdetail.entity.QProductDetail;
import com.example.evostyle.domain.product.productdetail.entity.QProductDetailOption;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class ProductQueryDslImpl implements ProductQueryDsl {
    private final JPAQueryFactory jpaQueryFactory;
    private final QProductDetail productDetail = QProductDetail.productDetail;
    private final QProductDetailOption productDetailOption = QProductDetailOption.productDetailOption;
    private final QOption option = QOption.option;
    private final QOptionGroup optionGroup = QOptionGroup.optionGroup;
    private final QProduct product = QProduct.product;

    @Override
    public void deleteProductById(Long productId) {
        jpaQueryFactory.delete(productDetailOption)
                .where(productDetailOption.productDetail.product.id.eq(productId))
                .execute();

        jpaQueryFactory.delete(productDetail)
                .where(productDetail.product.id.eq(productId))
                .execute();

        jpaQueryFactory.delete(option)
                .where(option.optionGroup.product.id.eq(productId))
                .execute();

        jpaQueryFactory.delete(optionGroup)
                .where(optionGroup.product.id.eq(productId))
                .execute();

        jpaQueryFactory.delete(product)
                .where(product.id.eq(productId))
                .execute();
    }
}
