package com.example.evostyle.domain.brand.service;

import com.example.evostyle.domain.brand.brandcategory.*;
import com.example.evostyle.domain.brand.dto.request.UpdateBrandCategoryRequest;
import com.example.evostyle.domain.brand.dto.response.CategoryInfo;
import com.example.evostyle.domain.brand.dto.response.UpdateBrandCategoryResponse;
import com.example.evostyle.domain.brand.entity.Brand;
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
@Transactional
public class BrandCategoryService {

    private final BrandCategoryRepository brandCategoryRepository;
    private final BrandCategoryMappingRepository brandCategoryMappingRepository;
    private final BrandRepository brandRepository;

    @Transactional
    public UpdateBrandCategoryResponse updateBrandCategories(
            List<UpdateBrandCategoryRequest> requestList,
            Long brandId
    ) {
        Brand brand = brandRepository.findById(brandId)
                .orElseThrow(() -> new NotFoundException(ErrorCode.BRAND_NOT_FOUND));

        if (requestList.size() > BrandCategoryLimit.MAX_CATEGORY_COUNT) {
            throw new BadRequestException(ErrorCode.CATEGORY_LIMIT_EXCEEDED);
        }

        for (UpdateBrandCategoryRequest request : requestList) {
            Long currentCategoryId = request.currentCategoryId();
            Long newCategoryId = request.newCategoryId();

            boolean currentExists = brandCategoryMappingRepository.existsByBrandIdAndBrandCategoryId(
                    brandId,
                    currentCategoryId
            );

            if (!currentExists) {
                throw new NotFoundException(ErrorCode.BRAND_CATEGORY_MAPPING_NOT_FOUND);
            }

            boolean newExists = brandCategoryMappingRepository.existsByBrandIdAndBrandCategoryId(
                    brandId,
                    newCategoryId
            );

            if (newExists) {
                throw new BadRequestException(ErrorCode.BRAND_CATEGORY_MAPPING_DUPLICATED);
            }

            BrandCategory newBrandCategory = brandCategoryRepository.findById(newCategoryId)
                    .orElseThrow(() -> new NotFoundException(ErrorCode.BRAND_CATEGORY_NOT_FOUND));

            BrandCategoryMapping brandCategoryMapping = brandCategoryMappingRepository.findByBrandIdAndBrandCategoryId(
                    brandId,
                    currentCategoryId
            ).orElseThrow(() -> new NotFoundException(ErrorCode.BRAND_CATEGORY_MAPPING_NOT_FOUND));

            brandCategoryMapping.update(newBrandCategory);
        }

        List<BrandCategoryMapping> brandCategoryMappingList = brandCategoryMappingRepository.findAllByBrandId(brandId);

        List<CategoryInfo> categoryInfoList = brandCategoryMappingList.stream()
                .map(mapping -> CategoryInfo.from(mapping.getBrandCategory()))
                .toList();

        return UpdateBrandCategoryResponse.from(brand, categoryInfoList);
    }
}
