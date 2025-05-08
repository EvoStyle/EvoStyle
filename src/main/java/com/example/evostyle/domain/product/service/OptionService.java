package com.example.evostyle.domain.product.service;

import com.example.evostyle.domain.member.entity.Member;
import com.example.evostyle.domain.member.repository.MemberRepository;
import com.example.evostyle.domain.product.dto.request.CreateOptionRequest;
import com.example.evostyle.domain.product.dto.request.UpdateOptionRequest;
import com.example.evostyle.domain.product.dto.response.OptionResponse;
import com.example.evostyle.domain.product.entity.Option;
import com.example.evostyle.domain.product.entity.OptionGroup;
import com.example.evostyle.domain.product.entity.ProductDetail;
import com.example.evostyle.domain.product.repository.OptionGroupRepository;
import com.example.evostyle.domain.product.repository.OptionRepository;
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
public class OptionService {

    private final MemberRepository memberRepository;
    private final OptionGroupRepository optionGroupRepository;
    private final OptionRepository optionRepository;
    private final ProductDetailService productDetailService;

    @Transactional
    public List<OptionResponse> createOption(Long memberId, Long optionGroupId, List<CreateOptionRequest> requestList){

        if(!memberRepository.existsById(memberId)){throw new NotFoundException(ErrorCode.MEMBER_NOT_FOUND);}

        OptionGroup optionGroup = optionGroupRepository.findById(optionGroupId).orElseThrow(() -> new NotFoundException(ErrorCode.OPTION_GROUP_NOT_FOUND));

        Long brandOwnerId = optionGroup.getProduct().getBrand().getMember().getId();
        if(!memberId.equals(brandOwnerId)){throw new UnauthorizedException(ErrorCode.NOT_BRAND_OWNER);}

        List<Option> optionList = requestList.stream().map(r -> Option.of(optionGroup, r.type())).toList();
        optionRepository.saveAll(optionList);
        productDetailService.createProductDetail(memberId, optionGroup.getProduct().getId());
        return optionList.stream().map(OptionResponse::from).toList();
    }

    public List<OptionResponse> readByOptionGroup(Long optionGroupId) {
        if (!optionGroupRepository.existsById(optionGroupId)){throw new NotFoundException(ErrorCode.OPTION_GROUP_NOT_FOUND);}
        return optionRepository.findByOptionGroupId(optionGroupId).stream().map(OptionResponse::from).toList();
    }

    @Transactional
    public OptionResponse updateOption(UpdateOptionRequest request, Long optionId) {
        Option option = optionRepository.findById(optionId).orElseThrow(() -> new NotFoundException(ErrorCode.OPTION_NOT_FOUND));

        option.updateType(request.type());
        return OptionResponse.from(option);
    }

    @Transactional
    public void deleteOption(Long optionId) {
       Option option = optionRepository.findById(optionId)
               .orElseThrow(() -> new NotFoundException(ErrorCode.OPTION_NOT_FOUND));

       option.delete();
    }

}
