package com.example.evostyle.domain.brand.service;

import com.example.evostyle.domain.brand.dto.request.CreateBrandRequest;
import com.example.evostyle.domain.brand.dto.request.UpdateOwnerBrandCategoryRequest;
import com.example.evostyle.domain.brand.dto.request.UpdateBrandNameRequest;
import com.example.evostyle.domain.brand.dto.response.*;
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
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

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
    public UpdateOwnerBrandCategoryResponse updateBrandCategories(UpdateOwnerBrandCategoryRequest request, Long brandId) {
        // Set 형식으로 기존 브랜드 카테고리 매핑 집합 조회
        Set<BrandCategoryMapping> currentBrandCategoryMappingSet = brandCategoryMappingRepository.findAllByBrandId(brandId);

        Brand brand = currentBrandCategoryMappingSet.stream()
                .findFirst()
                .map(BrandCategoryMapping::getBrand)
                .orElseThrow(() -> new NotFoundException(ErrorCode.BRAND_NOT_FOUND));

        // 매핑 집합에서 기존 카테고리 Id 집합 추출
        Set<Long> currentBrandCategoryIdSet = currentBrandCategoryMappingSet.stream()
                .map(mapping -> mapping.getBrandCategory().getId())
                .collect(Collectors.toSet());

        // 새로운 카테고리 Id 집합 선언
        Set<Long> newBrandCategoryIdSet = new HashSet<>(request.categoryIdList());

        // 기존에는 있으나 새로운 카테고리에 없는 항목 삭제 (예시) 기존 1,2,3 / 새로운 카테고리 2,3,4 → 1 빼고 2,3 삭제)
        currentBrandCategoryIdSet.removeAll(newBrandCategoryIdSet);

        // 새로운 카테고리에는 있으나 기존에는 없는 항목 삭제 (예시) 기존 1,2,3 / 새로운 카테고리 2,3,4 → 4 빼고 2,3 삭제)
        newBrandCategoryIdSet.removeAll(currentBrandCategoryIdSet);

        // 5. 삭제할 카테고리 매핑 리스트 만들기 (기존 카테고리에서 삭제할 항목)
        List<BrandCategoryMapping> brandCategoryMappingListToDelete = currentBrandCategoryMappingSet.stream()
                .filter(mapping -> currentBrandCategoryIdSet.contains(mapping.getBrandCategory().getId()))
                .toList();

        // 6. 삭제 실행
        brandCategoryMappingRepository.deleteAll(brandCategoryMappingListToDelete);

        // 7. 새로운 카테고리 추가
        List<BrandCategoryMapping> brandCategoryMappingListToAdd = newBrandCategoryIdSet.stream()
                .map(categoryId -> {
                    BrandCategory brandCategory = brandCategoryRepository.findById(categoryId)
                            .orElseThrow(() -> new NotFoundException(ErrorCode.BRAND_CATEGORY_NOT_FOUND));
                    return BrandCategoryMapping.of(brand, brandCategory);
                })
                .toList();

        // 8. 추가 실행
        brandCategoryMappingRepository.saveAll(brandCategoryMappingListToAdd);

        // 9. 카테고리 정보 목록 생성
        List<CategoryInfo> categoryInfoList = brandCategoryMappingListToAdd.stream()
                .map(mapping -> CategoryInfo.from(mapping.getBrandCategory()))
                .toList();

        return UpdateOwnerBrandCategoryResponse.from(brand, categoryInfoList);
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
