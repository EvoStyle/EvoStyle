package com.example.evostyle.domain.bookmark.dto.response;

import com.example.evostyle.domain.bookmark.entity.Bookmark;

public record ReadBookmarkResponse(Long id, Long brandId, String brandName) {
    public static ReadBookmarkResponse from(Bookmark bookmark) {
        return new ReadBookmarkResponse(
            bookmark.getId(),
            bookmark.getBrand().getId(),
            bookmark.getBrand().getName());
    }
}
