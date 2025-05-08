package com.example.evostyle.domain.brand.service;

import com.example.evostyle.domain.brand.dto.request.CreateBrandRequest;
import com.example.evostyle.domain.brand.dto.request.UpdateBrandNameRequest;
import com.example.evostyle.domain.brand.dto.request.UpdateOwnerBrandCategoryRequest;
import com.example.evostyle.domain.brand.dto.response.*;
import com.example.evostyle.domain.brand.entity.Brand;
import com.example.evostyle.domain.brand.entity.BrandCategory;
import com.example.evostyle.domain.brand.entity.BrandCategoryMapping;
import com.example.evostyle.domain.brand.repository.BrandCategoryRepository;
import com.example.evostyle.domain.brand.repository.BrandRepository;
import com.example.evostyle.domain.member.entity.Authority;
import com.example.evostyle.domain.member.entity.Member;
import com.example.evostyle.domain.member.repository.MemberRepository;
import com.example.evostyle.global.exception.BadRequestException;
import com.example.evostyle.global.exception.ErrorCode;
import com.example.evostyle.global.exception.NotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class BrandService {
    private final BrandRepository brandRepository;
    private final MemberRepository memberRepository;
    private final BrandCategoryRepository brandCategoryRepository;

    @Transactional
    public CreateBrandResponse createBrand(CreateBrandRequest request, Long memberId) {

        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new NotFoundException(ErrorCode.MEMBER_NOT_FOUND));

        if (member.getAuthority() != Authority.OWNER) {
            throw new BadRequestException(ErrorCode.FORBIDDEN_MEMBER_OPERATION);
        }

        List<BrandCategory> brandCategoryList = brandCategoryRepository.findAllById(request.categoryIdList());

        if (brandRepository.existsByName(request.name())) {
            throw new BadRequestException(ErrorCode.BRAND_NAME_DUPLICATED);
        }

        Brand brand = Brand.of(request.name(), member, request.slackWebHookUrl() ,brandCategoryList);

        brandRepository.save(brand);

        List<BrandCategoryMapping> brandCategoryMappingList = brandCategoryList.stream()
                .map(brandCategory -> BrandCategoryMapping.of(brand, brandCategory))
                .toList();

        brandCategoryRepository.saveBrandCategoryMappings(brandCategoryMappingList);

        List<BrandCategoryInfo> brandCategoryInfoList = brandCategoryList.stream()
                .map(BrandCategoryInfo::from)
                .toList();

        return CreateBrandResponse.from(brand, brandCategoryInfoList);
    }

    public List<ReadBrandResponse> readAllBrands() {

        List<Brand> brandList = brandRepository.findAll();

        return brandList.stream()
                .map(brand -> {
                    List<BrandCategoryInfo> brandCategoryInfoList = brandCategoryRepository.findCategoryInfoByBrand(brand);

                    return ReadBrandResponse.from(brand, brandCategoryInfoList);
                }).toList();
    }

    public ReadBrandResponse readBrandById(Long brandId) {

        Brand brand = findById(brandId);

        List<BrandCategoryInfo> brandCategoryInfoList = brandCategoryRepository.findCategoryInfoByBrand(brand);

        return ReadBrandResponse.from(brand, brandCategoryInfoList);
    }

    @Transactional
    public UpdateBrandNameResponse updateBrand(UpdateBrandNameRequest request, Long brandId, Long memberId) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new NotFoundException(ErrorCode.MEMBER_NOT_FOUND));

        if (member.getAuthority() != Authority.OWNER) {
            throw new BadRequestException(ErrorCode.FORBIDDEN_MEMBER_OPERATION);
        }

        Brand brand = findById(brandId);

        if (!brand.getMember().getId().equals(memberId)) {
            throw new BadRequestException(ErrorCode.FORBIDDEN_MEMBER_OPERATION);
        }

        if (brandRepository.existsByName(request.name())) {
            throw new BadRequestException(ErrorCode.BRAND_NAME_DUPLICATED);
        }

        brand.update(request.name());

        List<BrandCategoryInfo> brandCategoryInfoList = brandCategoryRepository.findCategoryInfoByBrand(brand);

        return UpdateBrandNameResponse.from(brand, brandCategoryInfoList);
    }

    @Transactional
    public UpdateOwnerBrandCategoryResponse updateBrandCategories(UpdateOwnerBrandCategoryRequest request, Long brandId) {

        Brand brand = findById(brandId);

        brandCategoryRepository.deleteAllByBrandId(brandId);

        List<BrandCategory> brandCategoryList = brandCategoryRepository.findAllById(request.categoryIdList());

        List<BrandCategoryMapping> brandCategoryMappingList = brandCategoryList.stream()
                .map(category -> BrandCategoryMapping.of(brand, category))
                .toList();

        brandCategoryRepository.saveBrandCategoryMappings(brandCategoryMappingList);

        List<BrandCategoryInfo> brandCategoryInfoList = brandCategoryList.stream()
                .map(BrandCategoryInfo::from)
                .toList();

        return UpdateOwnerBrandCategoryResponse.from(brand, brandCategoryInfoList);
    }

    @Transactional
    public void deleteBrand(Long brandId, Long memberId) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new NotFoundException(ErrorCode.MEMBER_NOT_FOUND));

        if (member.getAuthority() != Authority.OWNER) {
            throw new BadRequestException(ErrorCode.FORBIDDEN_MEMBER_OPERATION);
        }

        Brand brand = findById(brandId);

        if (!brand.getMember().getId().equals(memberId)) {
            throw new BadRequestException(ErrorCode.FORBIDDEN_MEMBER_OPERATION);
        }

        if (!brandRepository.existsById(brandId)) {
            throw new NotFoundException(ErrorCode.BRAND_NOT_FOUND);
        }
        brandCategoryRepository.deleteAllByBrandId(brandId);

        brandRepository.deleteById(brandId);
    }

    private Brand findById(Long brandId) {
        return brandRepository.findById(brandId)
                .orElseThrow(() -> new NotFoundException(ErrorCode.BRAND_NOT_FOUND));
    }
}
