package com.example.evostyle.domain.bookmark.entity;

import com.example.evostyle.domain.brand.entity.Brand;
import com.example.evostyle.domain.member.entity.Member;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Table(name = "bookmarks", uniqueConstraints = {
    @UniqueConstraint(columnNames = {"member_id", "brand_id"})})
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Bookmark {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "brand_id", nullable = false)
    private Brand brand;

    private Bookmark(Member member, Brand brand) {
        this.member = member;
        this.brand = brand;
    }

    public static Bookmark of(Member member, Brand brand) {
        return new Bookmark(member, brand);
    }
}
