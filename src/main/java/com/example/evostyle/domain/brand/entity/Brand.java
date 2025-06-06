package com.example.evostyle.domain.brand.entity;

import com.example.evostyle.common.entity.BaseEntity;
import com.example.evostyle.domain.member.entity.Member;
import com.example.evostyle.global.exception.BadRequestException;
import com.example.evostyle.global.exception.ErrorCode;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Getter
@Table(name = "brands")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Brand extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(columnDefinition = "BIGINT")
    private Long id;

    @Column(name = "name", unique = true, nullable = false)
    private String name;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;

    @Column(name = "url",nullable = false)
    private String slackWebHookUrl;

    private boolean isDeleted;

    private Brand(String name, Member member,String slackWebHookUrl) {
        this.name = name;
        this.member = member;
        this.slackWebHookUrl = slackWebHookUrl;
        this.isDeleted = false;
    }

    public static Brand of(
            String name,
            Member member,
            String slackWebHookUrl,
            List<BrandCategory> brandCategoryList
    ) {
        validateBrandCategoryLimit(brandCategoryList);
        return new Brand(name, member,slackWebHookUrl);
    }

    private static void validateBrandCategoryLimit(List<BrandCategory> brandCategoryList) {
        if (brandCategoryList.size() > BrandCategoryLimit.MAX_CATEGORY_COUNT) {
            throw new BadRequestException(ErrorCode.CATEGORY_LIMIT_EXCEEDED);
        }

        if (brandCategoryList.isEmpty()) {
            throw new BadRequestException(ErrorCode.NON_EXISTENT_CATEGORY);
        }
    }

    public void update(String name) {
        this.name = name;
    }

    public void deleteBrand() {
        this.isDeleted = true;
    }
}