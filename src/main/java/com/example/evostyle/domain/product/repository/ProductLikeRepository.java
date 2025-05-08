package com.example.evostyle.domain.product.repository;

import com.example.evostyle.domain.product.entity.ProductLike;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ProductLikeRepository extends JpaRepository<ProductLike, Long> {

    boolean existsByMemberIdAndProductId(Long memberId, Long productId);

    @Modifying
    @Query("DELETE FROM ProductLike p WHERE p.member.id = :memberId AND p.product.id = :productId")
    void deleteByMemberIdAndProductId(Long memberId, Long productId);

    List<ProductLike> findByMemberId(Long memberId);
}
