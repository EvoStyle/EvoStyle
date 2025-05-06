package com.example.evostyle.domain.product.optiongroup.service;

import com.example.evostyle.domain.product.optiongroup.dto.request.CreateOptionRequest;
import com.example.evostyle.domain.product.optiongroup.dto.request.UpdateOptionRequest;
import com.example.evostyle.domain.product.optiongroup.dto.response.OptionResponse;
import com.example.evostyle.domain.product.optiongroup.entity.Option;
import com.example.evostyle.domain.product.optiongroup.entity.OptionGroup;
import com.example.evostyle.domain.product.optiongroup.repository.OptionGroupRepository;
import com.example.evostyle.domain.product.optiongroup.repository.OptionRepository;
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
public class OptionService {

    private final OptionGroupRepository optionGroupRepository;
    private final OptionRepository optionRepository;
    private final ProductDetailService productDetailService;

    @Transactional
    public List<OptionResponse> createOptionGroup(Long optionGroupId, List<CreateOptionRequest> request) {

        if (!optionGroupRepository.existsById(optionGroupId)) {
            throw new NotFoundException(ErrorCode.OPTION_GROUP_NOT_FOUND);
        }
        OptionGroup optionGroup = optionGroupRepository.findById(optionGroupId).orElseThrow(() -> new NotFoundException(ErrorCode.OPTION_GROUP_NOT_FOUND));
        List<OptionResponse> optionResponseList = request.stream()
                .map(r -> Option.of(optionGroup, r.type()))
                .map(optionRepository::save)
                .map(OptionResponse::from)
                .toList();

        return null;
    }

    public List<OptionResponse> readByOptionGroup(Long optionGroupId) {
        if (!optionGroupRepository.existsById(optionGroupId)) {
            throw new NotFoundException(ErrorCode.OPTION_GROUP_NOT_FOUND);
        }

        return optionRepository.findOptionByOptionGroupId(optionGroupId).stream()
                .map(OptionResponse::from).toList();
    }

    @Transactional
    public OptionResponse updateOption(UpdateOptionRequest request, Long optionId) {
        Option option = optionRepository.findById(optionId)
                .orElseThrow(() -> new NotFoundException(ErrorCode.OPTION_NOT_FOUND));

        option.updateType(request.type());

        return OptionResponse.from(option);
    }

    @Transactional
    public void deleteOption(Long optionId) {
        if (!optionRepository.existsById(optionId)) {
            throw new NotFoundException(ErrorCode.OPTION_NOT_FOUND);
        }

        optionRepository.deleteById(optionId);
    }
}
