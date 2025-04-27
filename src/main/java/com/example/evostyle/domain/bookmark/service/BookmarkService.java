package com.example.evostyle.domain.bookmark.service;

import com.example.evostyle.domain.bookmark.dto.response.CreateBookmarkResponse;
import com.example.evostyle.domain.bookmark.dto.response.ReadBookmarkResponse;
import com.example.evostyle.domain.bookmark.entity.Bookmark;
import com.example.evostyle.domain.bookmark.repository.BookmarkRepository;
import com.example.evostyle.domain.brand.entity.Brand;
import com.example.evostyle.domain.brand.repository.BrandRepository;
import com.example.evostyle.domain.member.entity.Member;
import com.example.evostyle.domain.member.repository.MemberRepository;
import com.example.evostyle.global.exception.ConflictException;
import com.example.evostyle.global.exception.ErrorCode;
import com.example.evostyle.global.exception.NotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class BookmarkService {

    private final BookmarkRepository bookmarkRepository;
    private final BrandRepository brandRepository;
    private final MemberRepository memberRepository;

    @Transactional
    public CreateBookmarkResponse createBookmark(Long brandId, HttpServletRequest request) {
        Long loginMemberId = (Long) request.getAttribute("memberId");

        boolean isBookmarkExists = bookmarkRepository.existsByMemberIdAndBrandId(loginMemberId, brandId);

        if (isBookmarkExists) {
            throw new ConflictException(ErrorCode.BOOKMARK_ALREADY_EXISTS);
        }

        Member member = memberRepository.findByIdAndIsDeletedFalse(loginMemberId)
            .orElseThrow(() -> new NotFoundException(ErrorCode.MEMBER_NOT_FOUND));

        Brand brand = brandRepository.findByIdAndIsDeletedFalse(brandId)
            .orElseThrow(() -> new NotFoundException(ErrorCode.BRAND_NOT_FOUND));

        Bookmark bookmark = Bookmark.of(member, brand);

        bookmarkRepository.save(bookmark);

        return CreateBookmarkResponse.from(bookmark);
    }

    public List<ReadBookmarkResponse> readAllBookmarks(HttpServletRequest request) {
        Long loginMemberId = (Long) request.getAttribute("memberId");

        List<Bookmark> bookmarkList = bookmarkRepository.findByMemberId(loginMemberId);

        return bookmarkList.stream()
            .map(ReadBookmarkResponse::from)
            .toList();
    }

    @Transactional
    public Long deleteBookmark(Long brandId, HttpServletRequest request) {
        Long loginMemberId = (Long) request.getAttribute("memberId");

        bookmarkRepository.deleteByMemberIdAndBrandId(loginMemberId, brandId);

        Bookmark bookmark = bookmarkRepository.findByMemberIdAndBrandId(loginMemberId, brandId)
            .orElseThrow(() -> new NotFoundException(ErrorCode.BOOKMARK_NOT_FOUND));

        return bookmark.getId();
    }
}
