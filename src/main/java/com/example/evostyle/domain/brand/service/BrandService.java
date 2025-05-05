package com.example.evostyle.domain.brand.service;

import com.example.evostyle.domain.brand.dto.request.CreateBrandRequest;
import com.example.evostyle.domain.brand.dto.request.UpdateBrandNameRequest;
import com.example.evostyle.domain.brand.dto.response.CategoryInfo;
import com.example.evostyle.domain.brand.dto.response.CreateBrandResponse;
import com.example.evostyle.domain.brand.dto.response.ReadBrandResponse;
import com.example.evostyle.domain.brand.dto.response.UpdateBrandNameResponse;
import com.example.evostyle.domain.brand.entity.Brand;
import com.example.evostyle.domain.brand.entity.BrandCategory;
import com.example.evostyle.domain.brand.entity.BrandCategoryMapping;
import com.example.evostyle.domain.brand.repository.BrandCategoryMappingRepository;
import com.example.evostyle.domain.brand.repository.BrandCategoryQueryDslImpl;
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

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class BrandService {
    private final BrandRepository brandRepository;
    private final BrandCategoryQueryDslImpl brandCategoryQueryDsl;
    private final MemberRepository memberRepository;
    private final BrandCategoryRepository brandCategoryRepository;
    private final BrandCategoryMappingRepository brandCategoryMappingRepository;

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

        Brand brand = Brand.of(request.name(), member, brandCategoryList);

        brandRepository.save(brand);

        List<BrandCategoryMapping> brandCategoryMappingList = brandCategoryList.stream()
                .map(brandCategory -> BrandCategoryMapping.of(brand, brandCategory))
                .toList();

        brandCategoryMappingRepository.saveAll(brandCategoryMappingList);

        List<CategoryInfo> categoryInfoList = brandCategoryList.stream()
                .map(CategoryInfo::from)
                .toList();

        return CreateBrandResponse.from(brand, categoryInfoList);
    }

    public List<ReadBrandResponse> readAllBrands() {

        List<Brand> brandList = brandRepository.findAll();

        List<ReadBrandResponse> responseList = new ArrayList<>();

        responseList = brandList.stream()
                .map(brand -> {
                    List<CategoryInfo> categoryInfoList = brandCategoryQueryDsl.findCategoryInfoByBrand(brand);

                    return ReadBrandResponse.from(brand, categoryInfoList);
                }).toList();

        return responseList;
    }

    public ReadBrandResponse readBrandById(Long brandId) {

        Brand brand = findById(brandId);

        List<CategoryInfo> categoryInfoList = brandCategoryQueryDsl.findCategoryInfoByBrand(brand);

        return ReadBrandResponse.from(brand, categoryInfoList);
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

        List<CategoryInfo> categoryInfoList = brandCategoryQueryDsl.findCategoryInfoByBrand(brand);

        return UpdateBrandNameResponse.from(brand, categoryInfoList);
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
        brandCategoryMappingRepository.deleteByBrandId(brandId);

        brandRepository.deleteById(brandId);
    }

    private Brand findById(Long brandId) {
        return brandRepository.findById(brandId)
                .orElseThrow(() -> new NotFoundException(ErrorCode.BRAND_NOT_FOUND));
    }
}
