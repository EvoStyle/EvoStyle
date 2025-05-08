package com.example.evostyle.domain.product.service;

import com.example.evostyle.domain.brand.entity.Brand;
import com.example.evostyle.domain.brand.repository.BrandRepository;
import com.example.evostyle.domain.product.dto.request.CreateProductRequest;
import com.example.evostyle.domain.product.dto.request.UpdateOwnerProductCategoryRequest;
import com.example.evostyle.domain.product.dto.request.UpdateProductRequest;
import com.example.evostyle.domain.product.dto.response.ProductCategoryInfo;
import com.example.evostyle.domain.product.dto.response.ProductResponse;
import com.example.evostyle.domain.product.dto.response.UpdateOwnerProductCategoryResponse;
import com.example.evostyle.domain.product.entity.*;
import com.example.evostyle.domain.product.repository.*;
import com.example.evostyle.global.exception.ErrorCode;
import com.example.evostyle.global.exception.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ProductService {

    private final ProductCategoryRepository productCategoryRepository;
    private final ProductRepository productRepository;
    private final BrandRepository brandRepository;
    private final ProductDetailRepository productDetailRepository;
    private final ProductDetailOptionRepository productDetailOptionRepository;
    private final OptionGroupRepository optionGroupRepository;
    private final OptionRepository optionRepository;

    @Transactional
    public ProductResponse createProduct(Long memberId, CreateProductRequest request) {
        Brand brand = brandRepository.findById(request.brandId())
                .orElseThrow(() -> new NotFoundException(ErrorCode.BRAND_NOT_FOUND));

        Long brandOwnerId = brand.getMember().getId();
        if (!memberId.equals(brandOwnerId)) {
            throw new NotFoundException(ErrorCode.NOT_BRAND_OWNER);
        }

        List<ProductCategory> productCategoryList = productCategoryRepository.findAllById(request.categoryIdList());

        Product product = Product.of(brand, request.name(), request.price(), request.description(), productCategoryList);

        productRepository.save(product);

        List<ProductCategoryMapping> productCategoryMappingList = productCategoryList.stream()
                .map(productCategory -> ProductCategoryMapping.of(product, productCategory))
                .toList();

        productCategoryRepository.saveProductCategoryMappings(productCategoryMappingList);

        List<ProductCategoryInfo> productCategoryInfoList = productCategoryList.stream()
                .map(ProductCategoryInfo::from)
                .toList();

        return ProductResponse.from(product, productCategoryInfoList);
    }

    public ProductResponse readProduct(Long productId) {
        Product product = findProductById(productId);

        List<ProductCategoryInfo> productCategoryInfoList = productCategoryRepository.findCategoryInfoByProduct(product);

        return ProductResponse.from(product, productCategoryInfoList);
    }

    public List<ProductResponse> readByBrand(Long memberId, Long brandId) {
        Brand brand = brandRepository.findById(brandId)
                .orElseThrow(() -> new NotFoundException(ErrorCode.BRAND_NOT_FOUND));

        Long brandOwnerId = brand.getMember().getId();
        if (!memberId.equals(brandOwnerId)) {
            throw new NotFoundException(ErrorCode.NOT_BRAND_OWNER);
        }

        return null;
    }

    // todo
//        return productRepository.findByBrandId(brandId)
//                .stream().map(ProductResponse::from)
//                .toList();


    @Transactional
    public ProductResponse updateProduct(Long memberId, UpdateProductRequest request, Long productId) {

        Product product = findProductById(productId);

        Long brandOwnerId = product.getBrand().getMember().getId();
        if (!memberId.equals(brandOwnerId)) {
            throw new NotFoundException(ErrorCode.NOT_BRAND_OWNER);
        }

        product.update(request.name(), request.description(), request.price());

        List<ProductCategoryInfo> productCategoryInfoList = productCategoryRepository.findCategoryInfoByProduct(product);

        return ProductResponse.from(product, productCategoryInfoList);
    }

    @Transactional
    public UpdateOwnerProductCategoryResponse updateProductCategories(UpdateOwnerProductCategoryRequest request, Long productId) {
        Product product = findProductById(productId);

        productCategoryRepository.deleteAllByProductId(productId);

        List<ProductCategory> productCategoryList = productCategoryRepository.findAllById(request.categoryIdList());

        List<ProductCategoryMapping> productCategoryMappingList = productCategoryList.stream()
                .map(category -> ProductCategoryMapping.of(product, category))
                .toList();

        productCategoryRepository.saveProductCategoryMappings(productCategoryMappingList);

        List<ProductCategoryInfo> productCategoryInfoList = productCategoryList.stream()
                .map(ProductCategoryInfo::from)
                .toList();

        return UpdateOwnerProductCategoryResponse.from(product, productCategoryInfoList);
    }

    @Transactional
    public void deleteProduct(Long memberId, Long productId) {

        Product product = findProductById(productId);

        Long brandOwnerId = product.getBrand().getMember().getId();
        if (!memberId.equals(brandOwnerId)) {
            throw new NotFoundException(ErrorCode.NOT_BRAND_OWNER);
        }

        List<OptionGroup> optionGroupList = optionGroupRepository.findByProductIdAndIsDeletedFalse(productId);
        optionGroupList.forEach(OptionGroup::delete);

        List<Option> optionList = optionRepository.findByOptionGroupId(optionGroupList.stream().map(OptionGroup::getId).toList());
        optionList.forEach(Option::delete);

        List<ProductDetail> productDetailList = productDetailRepository.findByProductId(productId);
        productDetailList.forEach(ProductDetail::delete);

        productDetailOptionRepository
                .findByProductDetailIdIn(productDetailList.stream().map(ProductDetail::getId).toList())
                .forEach(ProductDetailOption::delete);
    }

    private Product findProductById(Long productId) {
        return productRepository.findById(productId)
                .orElseThrow(() -> new NotFoundException(ErrorCode.PRODUCT_NOT_FOUND));
    }
}
