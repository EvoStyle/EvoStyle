package com.example.evostyle.domain.brand.service;

import com.example.evostyle.domain.brand.dto.request.UpdateBrandCategoryRequest;
import com.example.evostyle.domain.brand.dto.response.CategoryInfo;
import com.example.evostyle.domain.brand.dto.response.UpdateBrandCategoryResponse;
import com.example.evostyle.domain.brand.entity.Brand;
import com.example.evostyle.domain.brand.entity.BrandCategory;
import com.example.evostyle.domain.brand.entity.BrandCategoryLimit;
import com.example.evostyle.domain.brand.entity.BrandCategoryMapping;
import com.example.evostyle.domain.brand.repository.BrandCategoryMappingRepository;
import com.example.evostyle.domain.brand.repository.BrandCategoryRepository;
import com.example.evostyle.domain.brand.repository.BrandRepository;
import com.example.evostyle.global.exception.BadRequestException;
import com.example.evostyle.global.exception.ErrorCode;
import com.example.evostyle.global.exception.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class BrandCategoryService {

    private final BrandCategoryRepository brandCategoryRepository;
    private final BrandCategoryMappingRepository brandCategoryMappingRepository;
    private final BrandRepository brandRepository;

    public List<CategoryInfo> readAllBrandCategories() {

        List<BrandCategory> brandCategoryList = brandCategoryRepository.findAll();

        return brandCategoryList.stream().map(CategoryInfo::from).toList();
    }

    @Transactional
    public UpdateBrandCategoryResponse updateBrandCategories(
            UpdateBrandCategoryRequest request,
            Long brandId
    ) {
        Brand brand = brandRepository.findById(brandId)
                .orElseThrow(() -> new NotFoundException(ErrorCode.BRAND_NOT_FOUND));

        if (request.categoryIdList().size() > BrandCategoryLimit.MAX_CATEGORY_COUNT) {
            throw new BadRequestException(ErrorCode.CATEGORY_LIMIT_EXCEEDED);
        }

        List<BrandCategoryMapping> currentBrandCategoryMappingList = brandCategoryMappingRepository.findAllByBrandId(brandId);

        brandCategoryMappingRepository.deleteAll(currentBrandCategoryMappingList);

        List<BrandCategoryMapping> newBrandCategoryMappingList = request.categoryIdList()
                .stream()
                .map(categoryId -> {
                    BrandCategory brandCategory = brandCategoryRepository.findById(categoryId)
                            .orElseThrow(() -> new NotFoundException(ErrorCode.BRAND_CATEGORY_NOT_FOUND));

                    return BrandCategoryMapping.of(brand, brandCategory);
                }).toList();

        brandCategoryMappingRepository.saveAll(newBrandCategoryMappingList);

        List<CategoryInfo> categoryInfoList = newBrandCategoryMappingList.stream()
                .map(mapping -> CategoryInfo.from(mapping.getBrandCategory()))
                .toList();

        return UpdateBrandCategoryResponse.from(brand, categoryInfoList);
    }
}
