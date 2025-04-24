package com.example.evostyle.domain.brand.service;

import com.example.evostyle.domain.brand.brandcategory.BrandCategory;
import com.example.evostyle.domain.brand.brandcategory.BrandCategoryMapping;
import com.example.evostyle.domain.brand.brandcategory.BrandCategoryMappingRepository;
import com.example.evostyle.domain.brand.brandcategory.BrandCategoryRepository;
import com.example.evostyle.domain.brand.dto.request.CreateBrandRequest;
import com.example.evostyle.domain.brand.dto.response.CategoryInfo;
import com.example.evostyle.domain.brand.dto.response.CreateBrandResponse;
import com.example.evostyle.domain.brand.entity.Brand;
import com.example.evostyle.domain.brand.repository.BrandRepository;
import com.example.evostyle.domain.member.entity.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BrandService {
    private final BrandRepository brandRepository;
    private final MemberRepository memberRepository;
    private final BrandCategoryRepository brandCategoryRepository;
    private final BrandCategoryMappingRepository brandCategoryMappingRepository;

    @Transactional
    public CreateBrandResponse createBrand(CreateBrandRequest requestDto) {

        Member member = memberRepository.findById(1L).orElseThrow();

        List<BrandCategory> brandCategoryList = brandCategoryRepository.findAllById(requestDto.categoryIdList());

        Brand brand = Brand.of(requestDto.name(), member, brandCategoryList);

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
}
