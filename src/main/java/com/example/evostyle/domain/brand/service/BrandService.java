package com.example.evostyle.domain.brand.service;

import com.example.evostyle.domain.brand.brandcategory.BrandCategory;
import com.example.evostyle.domain.brand.brandcategory.BrandCategoryRepository;
import com.example.evostyle.domain.brand.dto.request.CreateBrandRequest;
import com.example.evostyle.domain.brand.dto.response.CreateBrandResponse;
import com.example.evostyle.domain.brand.entity.Brand;
import com.example.evostyle.domain.brand.repository.BrandRepository;
import com.example.evostyle.domain.member.entity.Member;
import com.example.evostyle.domain.member.entity.MemberRepository;
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

    @Transactional
    public CreateBrandResponse createBrand(CreateBrandRequest requestDto) {

        Member member = memberRepository.findById(1L).orElseThrow();

        List<BrandCategory> brandCategoryList =  brandCategoryRepository.findAllById(requestDto.categoryIdList());

        // 이름 리스트만 추출하기
        List<String> categoryNameList = brandCategoryList.stream().map(BrandCategory::getName).toList();

        Brand brand = Brand.of(requestDto.name(), member);

        brandRepository.save(brand);

        return CreateBrandResponse.of(brand);
    }
}
