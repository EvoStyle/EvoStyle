package com.example.evostyle.domain.brand.service;

import com.example.evostyle.domain.brand.dto.request.BrandCategoryRequest;
import com.example.evostyle.domain.brand.dto.request.UpdateBrandCategoryRequest;
import com.example.evostyle.domain.brand.dto.response.CategoryInfo;
import com.example.evostyle.domain.brand.dto.response.UpdateBrandCategoryResponse;
import com.example.evostyle.domain.brand.entity.Brand;
import com.example.evostyle.domain.brand.entity.BrandCategory;
import com.example.evostyle.domain.brand.entity.BrandCategoryMapping;
import com.example.evostyle.domain.brand.repository.BrandCategoryMappingRepository;
import com.example.evostyle.domain.brand.repository.BrandCategoryRepository;
import com.example.evostyle.global.exception.ErrorCode;
import com.example.evostyle.global.exception.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class BrandCategoryService {

    private final BrandCategoryRepository brandCategoryRepository;
    private final BrandCategoryMappingRepository brandCategoryMappingRepository;

    public List<CategoryInfo> readAllBrandCategories() {

        List<BrandCategory> brandCategoryList = brandCategoryRepository.findAll();

        return brandCategoryList.stream().map(CategoryInfo::from).toList();
    }

    @Transactional
    public UpdateBrandCategoryResponse updateBrandCategories(
            UpdateBrandCategoryRequest request,
            Long brandId
    ) {
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

        return UpdateBrandCategoryResponse.from(brand, categoryInfoList);
    }

    @Transactional
    public Map<String, String> createBrandCategories(List<BrandCategoryRequest> brandCategoryRequest) {
        List<String> nameList = brandCategoryRequest.stream().map(BrandCategoryRequest::name).toList();
        Set<String> duplicated = brandCategoryRepository.findByNameIn(nameList).stream().map(BrandCategory::getName).collect(Collectors.toSet());
        Map<String, String> result = new LinkedHashMap<>();

        for (BrandCategoryRequest categoryRequest : brandCategoryRequest) {
            if (duplicated.contains(categoryRequest.name())) {
                result.put(categoryRequest.name(), "실패");
            } else {
                result.put(categoryRequest.name(), "성공");
            }
        }

        List<BrandCategory> brandCategoryList = result.entrySet().stream().
                filter(entry -> entry.getValue().equals("성공"))
                .map(entry -> BrandCategory.of(entry.getKey()))
                .toList();

        brandCategoryRepository.saveAll(brandCategoryList);

        return result;
    }
}