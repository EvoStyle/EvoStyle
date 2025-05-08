package com.example.evostyle.domain.product.service;

import com.example.evostyle.domain.brand.entity.Brand;
import com.example.evostyle.domain.brand.repository.BrandRepository;
import com.example.evostyle.domain.product.dto.request.CreateProductRequest;
import com.example.evostyle.domain.product.dto.request.UpdateProductRequest;
import com.example.evostyle.domain.product.dto.response.ProductCategoryInfo;
import com.example.evostyle.domain.product.dto.response.ProductResponse;
import com.example.evostyle.domain.product.entity.Product;
import com.example.evostyle.domain.product.entity.ProductCategory;
import com.example.evostyle.domain.product.entity.ProductCategoryMapping;
import com.example.evostyle.domain.product.repository.ProductCategoryRepository;
import com.example.evostyle.domain.product.repository.ProductRepository;
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

    @Transactional
    public ProductResponse createProduct(CreateProductRequest request) {
        Brand brand = brandRepository.findById(request.brandId())
                .orElseThrow(() -> new NotFoundException(ErrorCode.BRAND_NOT_FOUND));

        List<ProductCategory> productCategoryList = productCategoryRepository.findAllById(request.categoryIdList());

        Product product = Product.of(brand, request.name(), request.price(), request.description(), productCategoryList);

        productRepository.save(product);

        List<ProductCategoryMapping> productCategoryMappingList = productCategoryList.stream()
                .map(productCategory -> ProductCategoryMapping.of(product, productCategory))
                .toList();

        productCategoryRepository.saveBrandCategoryMappings(productCategoryMappingList);

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

    @Transactional
    public ProductResponse updateProduct(UpdateProductRequest request, Long productId) {

        Product product = findProductById(productId);

        product.update(request.name(), request.description(), request.price());

        List<ProductCategoryInfo> productCategoryInfoList = productCategoryRepository.findCategoryInfoByProduct(product);

        return ProductResponse.from(product, productCategoryInfoList);
    }

    @Transactional
    public void deleteProduct(Long productId) {
        if (!productRepository.existsById(productId)) {
            throw new NotFoundException(ErrorCode.PRODUCT_NOT_FOUND);
        }

        productRepository.deleteProductById(productId);
    }

    private Product findProductById(Long productId) {
        return productRepository.findById(productId)
                .orElseThrow(() -> new NotFoundException(ErrorCode.PRODUCT_NOT_FOUND));
    }
}
