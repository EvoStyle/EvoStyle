package com.example.evostyle.domain.bookmark.controller;

import com.example.evostyle.domain.bookmark.dto.response.CreateBookmarkResponse;
import com.example.evostyle.domain.bookmark.dto.response.ReadBookmarkResponse;
import com.example.evostyle.domain.bookmark.service.BookmarkService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

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
        Long memberId = (Long) request.getAttribute("memberId");

        CreateBookmarkResponse bookmarkResponse = bookmarkService.createBookmark(memberId, brandId);

        return ResponseEntity.status(HttpStatus.CREATED).body(bookmarkResponse);
    }

    @GetMapping
    public ResponseEntity<List<ReadBookmarkResponse>> readAllBookmarks(HttpServletRequest request) {
        Long memberId = (Long) request.getAttribute("memberId");

        List<ReadBookmarkResponse> bookmarkResponseList = bookmarkService.readAllBookmarks(memberId);

        return ResponseEntity.status(HttpStatus.OK).body(bookmarkResponseList);
    }

    @DeleteMapping
    public ResponseEntity<Map<String, Long>> deleteBookmark(
        @RequestParam Long brandId,
        HttpServletRequest request
    ) {
        Long memberId = (Long) request.getAttribute("memberId");

        Long deleteBookmarkId = bookmarkService.deleteBookmark(memberId, brandId);

        return ResponseEntity.status(HttpStatus.OK).body(Map.of("bookmarkId", deleteBookmarkId));
    }
}
