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
    public CreateBookmarkResponse createBookmark(Long memberId, Long brandId) {
        boolean isBookmarkExists = bookmarkRepository.existsByMemberIdAndBrandId(memberId, brandId);

        if (isBookmarkExists) {
            throw new ConflictException(ErrorCode.BOOKMARK_ALREADY_EXISTS);
        }

        Member member = memberRepository.findByIdAndIsDeletedFalse(memberId)
            .orElseThrow(() -> new NotFoundException(ErrorCode.MEMBER_NOT_FOUND));

        Brand brand = brandRepository.findByIdAndIsDeletedFalse(brandId)
            .orElseThrow(() -> new NotFoundException(ErrorCode.BRAND_NOT_FOUND));

        Bookmark bookmark = Bookmark.of(member, brand);

        bookmarkRepository.save(bookmark);

        return CreateBookmarkResponse.from(bookmark);
    }

    public List<ReadBookmarkResponse> readAllBookmarks(Long memberId) {
        List<Bookmark> bookmarkList = bookmarkRepository.findByMemberId(memberId);

        return bookmarkList.stream()
            .map(ReadBookmarkResponse::from)
            .toList();
    }

    @Transactional
    public Long deleteBookmark(Long memberId, Long brandId) {
        Bookmark bookmark = bookmarkRepository.findByMemberIdAndBrandId(memberId, brandId)
            .orElseThrow(() -> new NotFoundException(ErrorCode.BOOKMARK_NOT_FOUND));

        bookmarkRepository.deleteByMemberIdAndBrandId(bookmark.getMember().getId(), bookmark.getBrand().getId());

        return bookmark.getId();
    }
}
