package com.example.evostyle.domain.product.optiongroup.repository;

import com.example.evostyle.domain.product.optiongroup.entity.Option;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;


public interface OptionRepository extends JpaRepository<Option, Long> {

    @Query(
            "SELECT o.id FROM Option o WHERE o.optionGroup.id = :optionGroupId"
    )
    List<Long> findIdByOptionGroupId(@Param("optionGroupId") Long optionGroupId);

    List<Option> findOptionByOptionGroupId(Long optionGroupId);



    //프로덕트 디테일아이디를 가지고 있는 상세옵션을 찾고, 상세옵션이 가지고 있는 옵션을 가지고 온다

    @Query("""
    SELECT o
    FROM ProductDetailOption pdo
    JOIN pdo.option o
    WHERE pdo.productDetail.id = :productDetailId
""")
    List<Option> findByProductDetailId(@Param("productDetailId") Long productDetailId);

//    @Query("""
//            SELECT o
//            FROM ProductDetail p JOIN FETCH ProductDetailOption pd
//            ON p.id = pd.productDetail.id JOIN FETCH Option o
//            ON pd.option.id = o.id
//            WHERE p.id = :productDetailId
//            """)
//    List<Option> findByProductDetailId(@Param("productDetailId") Long productDetailId);


}
