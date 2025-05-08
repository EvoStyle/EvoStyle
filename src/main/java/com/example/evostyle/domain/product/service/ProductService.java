package com.example.evostyle.domain.product.service;

import com.example.evostyle.domain.brand.entity.Brand;
import com.example.evostyle.domain.brand.repository.BrandRepository;
import com.example.evostyle.domain.product.dto.request.CreateProductRequest;
import com.example.evostyle.domain.product.dto.request.UpdateProductRequest;
import com.example.evostyle.domain.product.dto.response.ProductResponse;
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
    private final ProductCategoryMappingRepository categoryMappingRepository;
    private final ProductRepository productRepository;
    private final BrandRepository brandRepository;
    private final OptionGroupRepository optionGroupRepository;
    private final OptionRepository optionRepository;
    private final ProductDetailRepository productDetailRepository;
    private final ProductDetailOptionRepository productDetailOptionRepository;

    @Transactional
    public ProductResponse createProduct(Long memberId, CreateProductRequest request){
        Brand brand = brandRepository.findById(request.brandId())
                .orElseThrow(() -> new NotFoundException(ErrorCode.BRAND_NOT_FOUND));

        Long brandOwnerId = brand.getMember().getId();
        if(!memberId.equals(brandOwnerId)){throw new NotFoundException(ErrorCode.NOT_BRAND_OWNER);}

        ProductCategory category = productCategoryRepository.findById(request.categoryId())
                .orElseThrow(() -> new NotFoundException(ErrorCode.PRODUCT_CATEGORY_NOT_FOUND));

        Product product = Product.of(brand, request.name(), request.price(), request.description());

        productRepository.save(product);
        categoryMappingRepository.save(ProductCategoryMapping.of(product, category));

        return ProductResponse.from(product);
    }


    public ProductResponse readProduct(Long productId){
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new NotFoundException(ErrorCode.PRODUCT_NOT_FOUND));

        return ProductResponse.from(product);
    }

    public List<ProductResponse> readByBrand(Long memberId, Long brandId){
        Brand brand = brandRepository.findById(brandId)
                .orElseThrow(() -> new NotFoundException(ErrorCode.BRAND_NOT_FOUND));

        Long brandOwnerId = brand.getMember().getId();
        if(!memberId.equals(brandOwnerId)){throw new NotFoundException(ErrorCode.NOT_BRAND_OWNER);}

        return productRepository.findByBrandId(brandId)
                .stream().map(ProductResponse::from).toList();
    }

    @Transactional
    public ProductResponse updateProduct(Long memberId, UpdateProductRequest request, Long productId){

        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new NotFoundException(ErrorCode.PRODUCT_NOT_FOUND));

        Long brandOwnerId = product.getBrand().getMember().getId();
        if(!memberId.equals(brandOwnerId)){throw new NotFoundException(ErrorCode.NOT_BRAND_OWNER);}

       product.update(request.name(), request.description(), request.price());

       return ProductResponse.from(product);
    }

    @Transactional
   public void deleteProduct(Long memberId, Long productId){

        Product product = productRepository.findById(productId).orElseThrow(() -> new NotFoundException(ErrorCode.PRODUCT_NOT_FOUND));

        Long brandOwnerId = product.getBrand().getMember().getId();
        if(!memberId.equals(brandOwnerId)){throw new NotFoundException(ErrorCode.NOT_BRAND_OWNER);}

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
}
