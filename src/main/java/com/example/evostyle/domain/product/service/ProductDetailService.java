package com.example.evostyle.domain.product.service;

import com.example.evostyle.domain.product.dto.request.UpdateProductDetailRequest;
import com.example.evostyle.domain.product.dto.response.ProductDetailResponse;
import com.example.evostyle.domain.product.entity.Product;
import com.example.evostyle.domain.product.optiongroup.dto.response.OptionResponse;
import com.example.evostyle.domain.product.optiongroup.entity.Option;
import com.example.evostyle.domain.product.optiongroup.entity.OptionGroup;
import com.example.evostyle.domain.product.optiongroup.repository.OptionGroupRepository;
import com.example.evostyle.domain.product.optiongroup.repository.OptionRepository;
import com.example.evostyle.domain.product.productdetail.entity.ProductDetail;
import com.example.evostyle.domain.product.productdetail.entity.ProductDetailOption;
import com.example.evostyle.domain.product.repository.ProductDetailOptionRepository;
import com.example.evostyle.domain.product.repository.ProductDetailRepository;
import com.example.evostyle.domain.product.repository.ProductRepository;
import com.example.evostyle.global.exception.ConflictException;
import com.example.evostyle.global.exception.ErrorCode;
import com.example.evostyle.global.exception.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ProductDetailService {

    private final ProductRepository productRepository;
    private final OptionGroupRepository optionGroupRepository;
    private final ProductDetailRepository productDetailRepository;
    private final ProductDetailOptionRepository productDetailOptionRepository;
    private final OptionRepository optionRepository;

    @Transactional
    public List<ProductDetailResponse> createProductDetail(Long productId) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new NotFoundException(ErrorCode.PRODUCT_NOT_FOUND));

        List<OptionGroup> optionGroupList = optionGroupRepository.findByProductId(productId);
        List<List<Long>> allOptionList = new ArrayList<>();

        for (OptionGroup optionGroup : optionGroupList) {
            List<Long> optionIdList = optionRepository.findOptionByOptionGroupId(optionGroup.getId())
                    .stream().map(Option::getId).toList();

            allOptionList.add(optionIdList);
        }

        generateCombinations(allOptionList, 0, new ArrayList<>(), product);

        return readByProductId(productId);
    }

    //싱픔이 가지는 옵션의 모든 조합을 만들어 저장한다
    @Transactional
    public void generateCombinations(List<List<Long>> allOptionList, int depth, List<Long> combinationList, Product product) {
        if (depth == allOptionList.size()) {
            ProductDetail productDetail = productDetailRepository.save(ProductDetail.of(product));

            for (Long optionId : combinationList) {
                Option option = optionRepository.findById(optionId)
                        .orElseThrow(() -> new NotFoundException(ErrorCode.OPTION_NOT_FOUND));

                productDetailOptionRepository.save(ProductDetailOption.of(productDetail, option));
            }
            return;
        }

        for (Long optionId : allOptionList.get(depth)) {
            combinationList.add(optionId);
            generateCombinations(allOptionList, depth + 1, combinationList, product);
            combinationList.remove(combinationList.size() - 1);
        }
    }

    public List<ProductDetailResponse> readByProductId(Long productId) {

        List<ProductDetail> productDetailList = productDetailRepository.findByProductId(productId);
        List<ProductDetailResponse> responses = new ArrayList<>();

        for (ProductDetail productDetail : productDetailList) {
            List<ProductDetailOption> productDetailOptionList = productDetailOptionRepository
                    .findByProductDetailId(productDetail.getId());

            List<Long> optionIds = productDetailOptionList.stream().map(o -> o.getOption().getId()).toList();

            List<OptionResponse> optionList = optionRepository.findAllById(optionIds)
                    .stream().map(OptionResponse::from).toList();

            responses.add(ProductDetailResponse.from(productDetail, optionList));
        }
        return responses;
    }


    public ProductDetailResponse readProductDetailById(Long productDetailId) {
        ProductDetail productDetail = productDetailRepository.findById(productDetailId)
                .orElseThrow(() -> new NotFoundException(ErrorCode.PRODUCT_DETAIL_NOT_FOUND));

        List<OptionResponse> optionResponseList = productDetailOptionRepository.findByProductDetailId(productDetailId).stream()
                .map(ProductDetailOption::getOption)
                .map(OptionResponse::from).toList();

        return ProductDetailResponse.from(productDetail, optionResponseList);
    }

    @Transactional
    public List<ProductDetailResponse> updateProductDetailStock(List<UpdateProductDetailRequest> requestList, Long productId) {

        for (UpdateProductDetailRequest request : requestList) {
            ProductDetail productDetail = productDetailRepository.findById(request.productDetailId())
                    .orElseThrow(() -> new NotFoundException(ErrorCode.PRODUCT_DETAIL_NOT_FOUND));

            if (productDetail.getProduct().getId() != productId) {
              throw new ConflictException(ErrorCode.PRODUCT_DETAIL_MISMATCH);
            }

            if (productDetail.getStock() != 0) {// 상품의 재고를 처음 설정하거나, 0이 아닌경우 수정불가
                throw new ConflictException(ErrorCode.STOCK_MODIFICATION_NOT_ALLOWED);
            } else {
                productDetail.setStock(request.stock());
            }
        }

        return readByProductId(productId);
    }
}
