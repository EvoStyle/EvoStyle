package com.example.evostyle.domain.product.optiongroup.repository;

import com.example.evostyle.domain.product.optiongroup.dto.response.OptionQueryDto;
import com.example.evostyle.domain.product.optiongroup.entity.Option;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;


public interface OptionRepository extends JpaRepository<Option, Long> {

    @Query(
            "SELECT o.id FROM Option o WHERE o.optionGroup.id = :optionGroupId"
    )
    List<Long> findIdByOptionGroupId(@Param("optionGroupId") Long optionGroupId);

    List<Option> findOptionByOptionGroupId(Long optionGroupId);


    @Query("""
            SELECT o
            FROM Option o
            WHERE o.optionGroup.id in(:optionGroupIdList)
            """)
    List<Option> findByOptionGroupId(@Param("optionGroupIdList") List<Long> optionGroupIdList);

    @Query("""
            SELECT new com.example.evostyle.domain.product.optiongroup.dto.response.OptionQueryDto(
                            pdo.productDetail.id , o.id, o.optionGroup.id , o.type
            )
            FROM ProductDetailOption pdo JOIN Option o
            ON pdo.option.id = o.id
            where pdo.productDetail.id in (:productDetailIdList)
            """)
    List<OptionQueryDto> findOptionByProductDetailId(@Param("productDetailIdList") List<Long> productDetailIdList);


    List<Option> findByOptionGroupIdIn(@Param("optionGroupId") List<Long> optionGroupId);

}
