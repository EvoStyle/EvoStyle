package com.example.evostyle.domain.product.service;

import com.example.evostyle.domain.product.dto.request.CreateProductCategoryRequest;
import com.example.evostyle.domain.product.dto.request.UpdateProductCategoryRequest;
import com.example.evostyle.domain.product.dto.response.ReadProductCategoryResponse;
import com.example.evostyle.domain.product.dto.response.UpdateProductCategoryResponse;
import com.example.evostyle.domain.product.entity.ProductCategory;
import com.example.evostyle.domain.product.repository.ProductCategoryRepository;
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
public class ProductCategoryService {

    private final ProductCategoryRepository productCategoryRepository;

    @Transactional
    public Map<String, String> createProductCategories(List<CreateProductCategoryRequest> requestList) {
        List<String> nameList = requestList.stream()
                .map(CreateProductCategoryRequest::name)
                .toList();

        Set<String> duplicated = productCategoryRepository.findByNameIn(nameList)
                .stream()
                .map(ProductCategory::getName)
                .collect(Collectors.toSet());

        Map<String, String> result = new LinkedHashMap<>();

        for (CreateProductCategoryRequest request : requestList) {
            if (duplicated.contains(request.name())) {
                result.put(request.name(), "실패");
            } else {
                result.put(request.name(), "성공");
            }
        }

        List<ProductCategory> productCategoryList = result.entrySet()
                .stream()
                .filter(entry -> entry.getValue().equals("성공"))
                .map(entry -> ProductCategory.of(entry.getKey()))
                .toList();

        productCategoryRepository.saveAll(productCategoryList);

        return result;
    }

    public List<ReadProductCategoryResponse> readAllProductCategories() {

        List<ProductCategory> productCategoryList = productCategoryRepository.findAll();

        return productCategoryList.stream().map(ReadProductCategoryResponse::from).toList();
    }

    public ReadProductCategoryResponse readProductCategoryById(Long productCategoryId) {

        ProductCategory productCategory = findById(productCategoryId);

        return ReadProductCategoryResponse.from(productCategory);
    }

    @Transactional
    public UpdateProductCategoryResponse updateProductCategory(UpdateProductCategoryRequest request, Long productCategoryId) {

        boolean isAlreadyExisting = productCategoryRepository.existsByName(request.name());

        if (isAlreadyExisting) {
            throw new BadRequestException(ErrorCode.PRODUCT_CATEGORY_DUPLICATE);
        }

        ProductCategory productCategory = findById(productCategoryId);

        productCategory.update(request.name());

        return UpdateProductCategoryResponse.from(productCategory);
    }

    @Transactional
    public void deleteProductCategory(Long productCategoryId) {
        ProductCategory productCategory = findById(productCategoryId);

        productCategoryRepository.delete(productCategory);
    }

    private ProductCategory findById(Long productCategoryId) {
        return productCategoryRepository.findById(productCategoryId)
                .orElseThrow(() -> new NotFoundException(ErrorCode.PRODUCT_CATEGORY_NOT_FOUND));
    }
}
