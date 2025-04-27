package com.example.evostyle.domain.bookmark.controller;

import com.example.evostyle.domain.bookmark.dto.response.CreateBookmarkResponse;
import com.example.evostyle.domain.bookmark.service.BookmarkService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/api/bookmarks")
@RestController
@RequiredArgsConstructor
public class BookmarkController {

    private final BookmarkService bookmarkService;

    @PostMapping
    public ResponseEntity<CreateBookmarkResponse> createBookmark(
        @RequestParam Long brandId,
        HttpServletRequest request
    ) {
        CreateBookmarkResponse bookmarkResponse = bookmarkService.createBookmark(brandId, request);

        return ResponseEntity.status(HttpStatus.CREATED).body(bookmarkResponse);
    }
}
