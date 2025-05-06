package com.example.evostyle.domain.product.optiongroup.service;

import com.example.evostyle.domain.product.entity.Product;

import com.example.evostyle.domain.product.optiongroup.dto.request.CreateOptionGroupRequest;
import com.example.evostyle.domain.product.optiongroup.dto.request.UpdateOptionGroupRequest;
import com.example.evostyle.domain.product.optiongroup.dto.response.CreateOptionGroupResponse;
import com.example.evostyle.domain.product.optiongroup.dto.response.OptionGroupResponse;
import com.example.evostyle.domain.product.optiongroup.dto.response.OptionResponse;
import com.example.evostyle.domain.product.optiongroup.entity.Option;
import com.example.evostyle.domain.product.optiongroup.entity.OptionGroup;

import com.example.evostyle.domain.product.optiongroup.repository.OptionGroupRepository;
import com.example.evostyle.domain.product.optiongroup.repository.OptionRepository;
import com.example.evostyle.domain.product.repository.ProductRepository;
import com.example.evostyle.domain.product.service.ProductDetailService;
import com.example.evostyle.global.exception.ErrorCode;
import com.example.evostyle.global.exception.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class OptionGroupService {

    private final OptionGroupRepository optionGroupRepository;
    private final OptionRepository optionRepository;
    private final ProductRepository productRepository;
    private final ProductDetailService productDetailService;

    @Transactional
    public CreateOptionGroupResponse createOptionGroupWithOptions(CreateOptionGroupRequest request, Long productId) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new NotFoundException(ErrorCode.PRODUCT_NOT_FOUND));

        OptionGroup optionGroup = optionGroupRepository.save(OptionGroup.of(request.name(), product));

        List<OptionResponse> optionResponseList = request.optionRequestList().stream()
                .map(r -> Option.of(optionGroup, r.type()))
                .map(optionRepository::save)
                .map(OptionResponse::from)
                .toList();

        return CreateOptionGroupResponse.from(optionGroup, optionResponseList);
    }

    public List<OptionGroupResponse> readOptionGroupByProduct(Long productId) {
        if (!productRepository.existsById(productId)) {
            throw new NotFoundException(ErrorCode.PRODUCT_NOT_FOUND);
        }

        return optionGroupRepository.findByProductId(productId)
                .stream().map(OptionGroupResponse::from).toList();
    }

    @Transactional
    public OptionGroupResponse updateOptionGroupName(UpdateOptionGroupRequest request, Long optionGroupId) {

        OptionGroup optionGroup = optionGroupRepository.findById(optionGroupId)
                .orElseThrow(() -> new NotFoundException(ErrorCode.OPTION_GROUP_NOT_FOUND));


        optionGroup.update(request.name());
        return OptionGroupResponse.from(optionGroup);
    }

    @Transactional
    public void deleteOptionGroup(Long optionGroupId) {
        if (!optionGroupRepository.existsById(optionGroupId)) {
            throw new NotFoundException(ErrorCode.OPTION_GROUP_NOT_FOUND);
        }

        List<Long> optionIdList = optionRepository.findIdByOptionGroupId(optionGroupId);
        optionRepository.deleteAllById(optionIdList);

        optionRepository.deleteById(optionGroupId);
    }
}
