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



}
