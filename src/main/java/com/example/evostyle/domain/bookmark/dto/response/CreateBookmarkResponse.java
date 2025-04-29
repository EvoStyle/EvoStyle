package com.example.evostyle.domain.bookmark.dto.response;

import com.example.evostyle.domain.bookmark.entity.Bookmark;

public record CreateBookmarkResponse(Long id, Long memberId, Long brandId) {
    public static CreateBookmarkResponse from(Bookmark bookmark) {
        return new CreateBookmarkResponse(
            bookmark.getId(),
            bookmark.getMember().getId(),
            bookmark.getBrand().getId());
    }
}
