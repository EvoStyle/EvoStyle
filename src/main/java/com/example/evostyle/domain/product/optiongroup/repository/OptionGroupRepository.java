package com.example.evostyle.domain.product.optiongroup.repository;

import com.example.evostyle.domain.product.optiongroup.entity.OptionGroup;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;


public interface OptionGroupRepository extends JpaRepository<OptionGroup, Long> {

   List<OptionGroup> findByProductId(Long productId);


   @Query("SELECT o.id FROM OptionGroup o WHERE o.product.id = :productId")
   List<Long> findOptionGroupIdByProductId(@Param("productId") Long productId);
}
