package com.example.evostyle.domain.product.service;

import com.example.evostyle.domain.member.repository.MemberRepository;
import com.example.evostyle.domain.product.entity.Product;

import com.example.evostyle.domain.product.dto.request.CreateOptionGroupRequest;
import com.example.evostyle.domain.product.dto.request.UpdateOptionGroupRequest;
import com.example.evostyle.domain.product.dto.response.CreateOptionGroupResponse;
import com.example.evostyle.domain.product.dto.response.OptionGroupResponse;
import com.example.evostyle.domain.product.dto.response.OptionResponse;
import com.example.evostyle.domain.product.entity.Option;
import com.example.evostyle.domain.product.entity.OptionGroup;

import com.example.evostyle.domain.product.repository.OptionGroupRepository;
import com.example.evostyle.domain.product.repository.OptionRepository;
import com.example.evostyle.domain.product.repository.ProductRepository;
import com.example.evostyle.global.exception.ErrorCode;
import com.example.evostyle.global.exception.NotFoundException;
import com.example.evostyle.global.exception.UnauthorizedException;
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
    private final MemberRepository memberRepository;

    private final OptionService optionService;

    @Transactional
    public CreateOptionGroupResponse createOptionGroupWithOptions(Long memberId,  Long productId, CreateOptionGroupRequest request) {

        if(!memberRepository.existsById(memberId)){throw new NotFoundException(ErrorCode.MEMBER_NOT_FOUND);}
        Product product = productRepository.findById(productId).orElseThrow(() -> new NotFoundException(ErrorCode.PRODUCT_NOT_FOUND));
        Long brandOwnerId = product.getBrand().getMember().getId();
        if(!memberId.equals(brandOwnerId)){throw new UnauthorizedException(ErrorCode.NOT_BRAND_OWNER);}

        OptionGroup optionGroup = optionGroupRepository.save(OptionGroup.of(request.name(), product));
        List<OptionResponse> optionResponseList = optionService.createOption(memberId, optionGroup.getId(), request.optionRequestList());

        return CreateOptionGroupResponse.from(optionGroup, optionResponseList);
    }

    public List<OptionGroupResponse> readOptionGroupByProduct(Long productId) {
        if (!productRepository.existsById(productId)) {throw new NotFoundException(ErrorCode.PRODUCT_NOT_FOUND);}
        return optionGroupRepository.findByProductIdAndIsDeletedFalse(productId).stream().map(OptionGroupResponse::from).toList();
    }

    @Transactional
    public OptionGroupResponse updateOptionGroupName(Long memberId, UpdateOptionGroupRequest request, Long optionGroupId) {

        if(!memberRepository.existsById(memberId)){throw new NotFoundException(ErrorCode.MEMBER_NOT_FOUND);}
        OptionGroup optionGroup = optionGroupRepository.findByIdAndIsDeletedFalse(optionGroupId).orElseThrow(() -> new NotFoundException(ErrorCode.OPTION_GROUP_NOT_FOUND));

        Long brandOwnerId = optionGroup.getProduct().getBrand().getMember().getId();
        if(!memberId.equals(brandOwnerId)){throw new UnauthorizedException(ErrorCode.NOT_BRAND_OWNER);}

        optionGroup.update(request.name());
        return OptionGroupResponse.from(optionGroup);
    }

    @Transactional
    public void deleteOptionGroup(Long memberId, Long optionGroupId) {
        if(!memberRepository.existsById(memberId)){throw new NotFoundException(ErrorCode.MEMBER_NOT_FOUND);}

        OptionGroup optionGroup = optionGroupRepository.findByIdAndIsDeletedFalse(optionGroupId).orElseThrow(() -> new NotFoundException(ErrorCode.OPTION_GROUP_NOT_FOUND));

        Long brandOwnerId = optionGroup.getProduct().getBrand().getMember().getId();
        if(!memberId.equals(brandOwnerId)){throw new UnauthorizedException(ErrorCode.NOT_BRAND_OWNER);}

        optionRepository.findByOptionGroupId(optionGroupId).forEach(Option::delete);
        optionGroup.delete();
    }
}
