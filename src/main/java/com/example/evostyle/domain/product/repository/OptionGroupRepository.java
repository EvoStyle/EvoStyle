package com.example.evostyle.domain.product.repository;

import com.example.evostyle.domain.product.entity.OptionGroup;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface OptionGroupRepository extends JpaRepository<OptionGroup, Long> {

    @Query("""
            SELECT o.id
            FROM OptionGroup o
            WHERE o.product.id = :productId
            """)
    List<Long> findIdByProductId(Long productId);

    List<OptionGroup> findByProductIdAndIsDeletedFalse(Long productId);

    Optional<OptionGroup> findByIdAndIsDeletedFalse(Long id);
}
