package com.example.evostyle.domain.product.service;

import com.example.evostyle.domain.brand.entity.Brand;
import com.example.evostyle.domain.brand.repository.BrandRepository;
import com.example.evostyle.domain.product.dto.request.CreateProductRequest;
import com.example.evostyle.domain.product.dto.response.ProductResponse;
import com.example.evostyle.domain.product.entity.Product;
import com.example.evostyle.domain.product.productcategory.entity.ProductCategory;
import com.example.evostyle.domain.product.productcategory.entity.ProductCategoryMapping;
import com.example.evostyle.domain.product.productcategory.repository.ProductCategoryMappingRepository;
import com.example.evostyle.domain.product.productcategory.repository.ProductCategoryRepository;
import com.example.evostyle.domain.product.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductCategoryRepository productCategoryRepository;
    private final ProductCategoryMappingRepository categoryMappingRepository;
    private final ProductRepository productRepository ;
    private final BrandRepository brandRepository;

    @Transactional
    public ProductResponse createProduct(CreateProductRequest request, Long brandId, Long categoryId){
        Brand brand = brandRepository.findById(brandId).orElseThrow(()->new RuntimeException("브랜드가 존재하지 않습니다"));
        ProductCategory category = productCategoryRepository.findById(categoryId).orElseThrow(()->new RuntimeException("카테고리가 존재하지 않습니다"));

        Product product = Product.of(brand, categoryId, request.name(), request.price(), request.description());
        Product savedProduct = productRepository.save(product);

        categoryMappingRepository.save(ProductCategoryMapping.of(product, category));

        return ProductResponse.from(savedProduct);
    }

}
