package com.example.evostyle.domain.brand.service;

import com.example.evostyle.domain.brand.dto.request.CreateBrandCategoryRequest;
import com.example.evostyle.domain.brand.dto.request.UpdateBrandCategoryRequest;
import com.example.evostyle.domain.brand.dto.response.ReadProductCategoryResponse;
import com.example.evostyle.domain.brand.dto.response.UpdateBrandCategoryResponse;
import com.example.evostyle.domain.brand.entity.BrandCategory;
import com.example.evostyle.domain.brand.repository.BrandCategoryRepository;
import com.example.evostyle.global.exception.BadRequestException;
import com.example.evostyle.global.exception.ErrorCode;
import com.example.evostyle.global.exception.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class BrandCategoryService {

    private final BrandCategoryRepository brandCategoryRepository;

    @Transactional
    public Map<String, String> createBrandCategories(List<CreateBrandCategoryRequest> requestList) {
        List<String> nameList = requestList.stream()
                .map(CreateBrandCategoryRequest::name)
                .toList();

        Set<String> duplicated = brandCategoryRepository.findByNameIn(nameList)
                .stream()
                .map(BrandCategory::getName)
                .collect(Collectors.toSet());

        Map<String, String> result = new LinkedHashMap<>();

        for (CreateBrandCategoryRequest request : requestList) {
            if (duplicated.contains(request.name())) {
                result.put(request.name(), "실패");
            } else {
                result.put(request.name(), "성공");
            }
        }

        List<BrandCategory> brandCategoryList = result.entrySet()
                .stream()
                .filter(entry -> entry.getValue().equals("성공"))
                .map(entry -> BrandCategory.of(entry.getKey()))
                .toList();

        brandCategoryRepository.saveAll(brandCategoryList);

        return result;
    }

    public List<ReadProductCategoryResponse> readAllBrandCategories() {

        List<BrandCategory> brandCategoryList = brandCategoryRepository.findAll();

        return brandCategoryList.stream().map(ReadProductCategoryResponse::from).toList();
    }

    public ReadProductCategoryResponse readBrandCategoryById(Long brandCategoryId) {
        BrandCategory brandCategory = findById(brandCategoryId);

        return ReadProductCategoryResponse.from(brandCategory);
    }

    @Transactional
    public UpdateBrandCategoryResponse updateBrandCategory(UpdateBrandCategoryRequest request, Long brandCategoryId) {

        boolean isAlreadyExisting = brandCategoryRepository.existsByName(request.name());

        if (isAlreadyExisting) {
            throw new BadRequestException(ErrorCode.BRAND_CATEGORY_DUPLICATE);
        }

        BrandCategory brandCategory = findById(brandCategoryId);

        brandCategory.update(request.name());

        return UpdateBrandCategoryResponse.from(brandCategory);
    }

    @Transactional
    public void deleteBrandCategory(Long brandCategoryId) {
        BrandCategory brandCategory = findById(brandCategoryId);

        brandCategoryRepository.delete(brandCategory);
    }

    private BrandCategory findById(Long brandCategoryId) {
        return brandCategoryRepository.findById(brandCategoryId)
                .orElseThrow(() -> new NotFoundException(ErrorCode.BRAND_CATEGORY_NOT_FOUND));
    }
}