package com.example.evostyle.domain.product.service;

import com.example.evostyle.domain.product.dto.request.CreateProductDetailRequest;
import com.example.evostyle.domain.product.dto.response.ProductDetailResponse;
import com.example.evostyle.domain.product.entity.Product;
import com.example.evostyle.domain.product.optiongroup.dto.response.OptionResponse;
import com.example.evostyle.domain.product.optiongroup.entity.Option;
import com.example.evostyle.domain.product.optiongroup.repository.OptionGroupRepository;
import com.example.evostyle.domain.product.optiongroup.repository.OptionRepository;
import com.example.evostyle.domain.product.productdetail.entity.ProductDetail;
import com.example.evostyle.domain.product.productdetail.entity.ProductDetailOption;
import com.example.evostyle.domain.product.repository.ProductDetailOptionRepository;
import com.example.evostyle.domain.product.repository.ProductDetailRepository;
import com.example.evostyle.domain.product.repository.ProductRepository;
import com.example.evostyle.global.exception.ErrorCode;
import com.example.evostyle.global.exception.InvalidException;
import com.example.evostyle.global.exception.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.HashSet;
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
    public ProductDetailResponse createProductDetail(CreateProductDetailRequest request, Long productId) {

        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new NotFoundException(ErrorCode.PRODUCT_NOT_FOUND));


        List<Option> optionList = optionRepository.findAllById(request.optionListId());
        HashSet<Long> optionIdSet = new HashSet<>(optionList.stream().map(Option::getId).toList());

        //입력받은 옵션아이디가 db에 존재하는 지 확인한다
       if(!optionIdSet.containsAll(request.optionListId())){
           throw new NotFoundException(ErrorCode.OPTION_NOT_FOUND);
       }

        List<Long> optionGroupIdList = optionGroupRepository.findOptionGroupIdByProductId(productId);
        HashSet<Long> uniqueOptionGroupIdSet = new HashSet<>();

        for (Option op : optionList) {
            Long optionGroupId = op.getOptionGroup().getId();

            //선택한 옵션이 해당상품의 하위 옵션이 맞는지 확인한다
            if (!optionGroupIdList.contains(optionGroupId)) {
                throw new InvalidException(ErrorCode.INVALID_PRODUCT_OPTION);
            }

            //하나의 옵션그룹의 여러개의 값을 선택하지 않았는지 확인한다
            if(!uniqueOptionGroupIdSet.add(optionGroupId)){
                throw new InvalidException(ErrorCode.MULTIPLE_OPTION_SELECTED);
            }
        }

        ProductDetail productDetail = ProductDetail.of(product, request.stock());
        productDetailRepository.save(productDetail);

        optionList.stream().map(o -> ProductDetailOption.of(productDetail, o))
                  .forEach(productDetailOptionRepository::save);

        List<OptionResponse> optionResponseList = optionList.stream().map(OptionResponse::from).toList();
        return ProductDetailResponse.from(productDetail, optionResponseList);
    }
}
