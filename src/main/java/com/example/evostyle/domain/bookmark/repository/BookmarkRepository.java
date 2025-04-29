package com.example.evostyle.domain.bookmark.repository;

import com.example.evostyle.domain.bookmark.entity.Bookmark;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface BookmarkRepository extends JpaRepository<Bookmark, Long> {

    boolean existsByMemberIdAndBrandId(Long memberId, Long brandId);

    List<Bookmark> findByMemberId(Long memberId);

    Optional<Bookmark> findByMemberIdAndBrandId(Long memberId, Long brandId);

    void deleteByMemberIdAndBrandId(Long memberId, Long brandId);
}
