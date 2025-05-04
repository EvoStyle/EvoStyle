package com.example.evostyle.domain.product.service;

import com.example.evostyle.domain.brand.entity.Brand;
import com.example.evostyle.domain.brand.repository.BrandRepository;
import com.example.evostyle.domain.member.entity.Member;
import com.example.evostyle.domain.member.repository.MemberRepository;
import com.example.evostyle.domain.product.dto.request.CreateProductRequest;
import com.example.evostyle.domain.product.dto.request.UpdateProductRequest;
import com.example.evostyle.domain.product.dto.response.ProductResponse;
import com.example.evostyle.domain.product.entity.Product;
import com.example.evostyle.domain.product.optiongroup.entity.Option;
import com.example.evostyle.domain.product.optiongroup.entity.OptionGroup;
import com.example.evostyle.domain.product.optiongroup.repository.OptionGroupRepository;
import com.example.evostyle.domain.product.optiongroup.repository.OptionRepository;
import com.example.evostyle.domain.product.productcategory.entity.ProductCategory;
import com.example.evostyle.domain.product.productcategory.entity.ProductCategoryMapping;
import com.example.evostyle.domain.product.productcategory.repository.ProductCategoryMappingRepository;
import com.example.evostyle.domain.product.productcategory.repository.ProductCategoryRepository;
import com.example.evostyle.domain.product.productdetail.entity.ProductDetail;
import com.example.evostyle.domain.product.productdetail.entity.ProductDetailOption;
import com.example.evostyle.domain.product.repository.ProductDetailOptionRepository;
import com.example.evostyle.domain.product.repository.ProductDetailRepository;
import com.example.evostyle.domain.product.repository.ProductRepository;
import com.example.evostyle.global.exception.ErrorCode;
import com.example.evostyle.global.exception.NotFoundException;
import com.example.evostyle.global.exception.UnauthorizedException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

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
    private final OptionGroupRepository optionGroupRepository;
    private final OptionRepository optionRepository;
    private final ProductDetailRepository productDetailRepository;
    private final ProductDetailOptionRepository productDetailOptionRepository;

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
   public void deleteProduct(Long productId){
        if(!productRepository.existsById(productId)){
            throw new NotFoundException(ErrorCode.PRODUCT_NOT_FOUND);
        }

        List<OptionGroup> optionGroupList = optionGroupRepository.findByProductId(productId);
        List<Option> optionList = optionRepository.findByOptionGroupId(optionGroupList.stream().map(OptionGroup::getId).toList());
        List<ProductDetail> productDetailList = productDetailRepository.findByProductId(productId);
        List<ProductDetailOption> productDetailOptionList = productDetailOptionRepository
                .findByProductDetailIdIn(productDetailList.stream().map(ProductDetail::getId).toList());

        productDetailOptionRepository.deleteAll(productDetailOptionList);
        productDetailRepository.deleteAll(productDetailList);
        optionRepository.deleteAll(optionList);
        optionGroupRepository.deleteAll(optionGroupList);
        productRepository.deleteById(productId);
    }
}
