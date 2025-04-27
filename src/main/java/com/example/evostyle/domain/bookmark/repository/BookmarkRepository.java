package com.example.evostyle.domain.bookmark.repository;

import com.example.evostyle.domain.bookmark.entity.Bookmark;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BookmarkRepository extends JpaRepository<Bookmark, Long> {

    boolean existsByMemberIdAndBrandId(Long memberId, Long brandId);
}
