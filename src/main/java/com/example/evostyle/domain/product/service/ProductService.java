package com.example.evostyle.domain.product.service;

import com.example.evostyle.domain.brand.entity.Brand;
import com.example.evostyle.domain.brand.repository.BrandRepository;
import com.example.evostyle.domain.product.dto.request.CreateProductRequest;
import com.example.evostyle.domain.product.dto.request.UpdateProductRequest;
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
@Transactional
public class ProductService {

    private final ProductCategoryRepository productCategoryRepository;
    private final ProductCategoryMappingRepository categoryMappingRepository;
    private final ProductRepository productRepository ;
    private final BrandRepository brandRepository;


    public ProductResponse createProduct(CreateProductRequest request){
        Brand brand = brandRepository.findById(request.brandId()).orElseThrow(()->new RuntimeException("브랜드가 존재하지 않습니다"));
        ProductCategory category = productCategoryRepository.findById(request.categoryId()).orElseThrow(()->new RuntimeException("카테고리가 존재하지 않습니다"));

        Product product = Product.of(brand, request.name(), request.price(), request.description());
        Product savedProduct = productRepository.save(product);

        categoryMappingRepository.save(ProductCategoryMapping.of(product, category));

        return ProductResponse.from(savedProduct);
    }

    @Transactional(readOnly = true)
    public ProductResponse readProduct(Long productId){
        Product product = productRepository.findById(productId).orElseThrow(() -> new RuntimeException("존재하지 않는 상품입니다"));

        return ProductResponse.from(product);
    }

    public ProductResponse updateProduct(UpdateProductRequest request, Long productId){

       Product product = productRepository.findById(productId)
               .orElseThrow(()-> new RuntimeException("존재하지 않는 상품입니다"));

       product.update(request.name(), request.description(), request.price());

       return ProductResponse.from(product);
    }

   public void deleteProduct(Long productId){
        if(!productRepository.existsById(productId)){
            throw new RuntimeException("존재하지 않는 상품입니다");
        }
        productRepository.deleteById(productId);
   }
}
