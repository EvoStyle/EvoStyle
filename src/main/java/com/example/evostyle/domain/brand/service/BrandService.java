package com.example.evostyle.domain.brand.service;

import com.example.evostyle.domain.brand.brandcategory.BrandCategory;
import com.example.evostyle.domain.brand.brandcategory.BrandCategoryMapping;
import com.example.evostyle.domain.brand.brandcategory.BrandCategoryMappingRepository;
import com.example.evostyle.domain.brand.brandcategory.BrandCategoryRepository;
import com.example.evostyle.domain.brand.dto.request.CreateBrandRequest;
import com.example.evostyle.domain.brand.dto.response.CategoryInfo;
import com.example.evostyle.domain.brand.dto.response.CreateBrandResponse;
import com.example.evostyle.domain.brand.dto.response.ReadBrandResponse;
import com.example.evostyle.domain.brand.entity.Brand;
import com.example.evostyle.domain.brand.repository.BrandRepository;
import com.example.evostyle.domain.member.entity.Member;
import com.example.evostyle.domain.member.repository.MemberRepository;
import com.example.evostyle.global.exception.ErrorCode;
import com.example.evostyle.global.exception.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class BrandService {
    private final BrandRepository brandRepository;
    private final MemberRepository memberRepository;
    private final BrandCategoryRepository brandCategoryRepository;
    private final BrandCategoryMappingRepository brandCategoryMappingRepository;

    @Transactional
    public CreateBrandResponse createBrand(CreateBrandRequest request) {

        Long memberId = 1L;

        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new NotFoundException(ErrorCode.MEMBER_NOT_FOUND));

        List<BrandCategory> brandCategoryList = brandCategoryRepository.findAllById(request.categoryIdList());

        Brand brand = Brand.of(request.name(), member, brandCategoryList);

        brandRepository.save(brand);

        List<BrandCategoryMapping> brandCategoryMappingList = brandCategoryList.stream()
                .map(brandCategory -> BrandCategoryMapping.of(brand, brandCategory))
                .toList();

        brandCategoryMappingRepository.saveAll(brandCategoryMappingList);

        // 카테고리 정보를 Id, name 묶어서 추출
        List<CategoryInfo> categoryInfoList = brandCategoryList.stream()
                .map(CategoryInfo::from)
                .toList();

        return CreateBrandResponse.from(brand, categoryInfoList);
    }

    public List<ReadBrandResponse> readAllBrands() {

        List<Brand> brandList = brandRepository.findByIsDeletedFalse();

        return brandList.stream().map(ReadBrandResponse::from).toList();
    }

    public ReadBrandResponse readBrandById(Long brandId) {

        Brand brand = brandRepository.findByIdAndIsDeletedFalse(brandId)
                .orElseThrow(() -> new NotFoundException(ErrorCode.BRAND_NOT_FOUND));

        return ReadBrandResponse.from(brand);
    }
}
