package com.example.evostyle.domain.product.service;

import com.example.evostyle.domain.brand.entity.Brand;
import com.example.evostyle.domain.brand.repository.BrandRepository;
import com.example.evostyle.domain.member.entity.Member;
import com.example.evostyle.domain.member.repository.MemberRepository;
import com.example.evostyle.domain.product.dto.request.CreateProductRequest;
import com.example.evostyle.domain.product.dto.request.UpdateProductRequest;
import com.example.evostyle.domain.product.dto.response.ProductResponse;
import com.example.evostyle.domain.product.entity.Product;
import com.example.evostyle.domain.product.productcategory.entity.ProductCategory;
import com.example.evostyle.domain.product.productcategory.entity.ProductCategoryMapping;
import com.example.evostyle.domain.product.productcategory.repository.ProductCategoryMappingRepository;
import com.example.evostyle.domain.product.productcategory.repository.ProductCategoryRepository;
import com.example.evostyle.domain.product.repository.ProductRepository;
import com.example.evostyle.global.exception.ErrorCode;
import com.example.evostyle.global.exception.NotFoundException;
import com.example.evostyle.global.exception.UnauthorizedException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ProductService {

    private final ProductCategoryRepository productCategoryRepository;
    private final ProductCategoryMappingRepository categoryMappingRepository;
    private final ProductRepository productRepository;
    private final BrandRepository brandRepository;
    private final MemberRepository memberRepository;

    @Transactional
    public ProductResponse createProduct(CreateProductRequest request, Long memberId) {

        log.info("memberId : " + memberId);

        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new NotFoundException(ErrorCode.MEMBER_NOT_FOUND));

        Brand brand = brandRepository.findById(request.brandId())
                .orElseThrow(() -> new NotFoundException(ErrorCode.BRAND_NOT_FOUND));

        ProductCategory category = productCategoryRepository.findById(request.categoryId())
                .orElseThrow(() -> new NotFoundException(ErrorCode.PRODUCT_CATEGORY_NOT_FOUND));

        Product product = Product.of(brand, request.name(), request.price(), request.description());
        productRepository.save(product);

        categoryMappingRepository.save(ProductCategoryMapping.of(product, category));

        return ProductResponse.from(product);
    }


    public ProductResponse readProduct(Long productId) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new NotFoundException(ErrorCode.PRODUCT_NOT_FOUND));

        return ProductResponse.from(product);
    }

    @Transactional
    public ProductResponse updateProduct(UpdateProductRequest request, Long productId, Long memberId) {

        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new NotFoundException(ErrorCode.MEMBER_NOT_FOUND));

        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new NotFoundException(ErrorCode.PRODUCT_NOT_FOUND));

//        if(product.getBrand().getMember().getId() != memberId){
//            throw new UnauthorizedException(ErrorCode.);
//        }

        product.update(request.name(), request.description(), request.price());

        return ProductResponse.from(product);
    }

    @Transactional
    public void deleteProduct(Long productId, Long memberId) {

        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new NotFoundException(ErrorCode.MEMBER_NOT_FOUND));

        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new NotFoundException(ErrorCode.PRODUCT_NOT_FOUND));

        //        if(product.getBrand().getMember().getId() != memberId){
//            throw new UnauthorizedException(ErrorCode.);
//        }

        productRepository.deleteById(productId);
    }
}
