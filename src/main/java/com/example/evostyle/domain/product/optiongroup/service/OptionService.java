package com.example.evostyle.domain.product.optiongroup.service;

import com.example.evostyle.domain.product.optiongroup.dto.request.UpdateOptionRequest;
import com.example.evostyle.domain.product.optiongroup.dto.response.OptionResponse;
import com.example.evostyle.domain.product.optiongroup.entity.Option;
import com.example.evostyle.domain.product.optiongroup.repository.OptionGroupRepository;
import com.example.evostyle.domain.product.optiongroup.repository.OptionRepository;
import com.example.evostyle.global.exception.ErrorCode;
import com.example.evostyle.global.exception.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class OptionService {

    private final OptionGroupRepository optionGroupRepository;
    private final OptionRepository optionRepository;

    public List<OptionResponse> readByOptionGroup(Long optionGroupId){
        if(!optionGroupRepository.existsById(optionGroupId)){
            throw new NotFoundException(ErrorCode.OPTION_GROUP_NOT_FOUND);
        }

        return optionRepository.findOptionByOptionGroupId(optionGroupId).stream()
                .map(OptionResponse::from).toList();
    }

    public OptionResponse updateOption(UpdateOptionRequest request, Long optionId){
        Option option = optionRepository.findById(optionId)
                .orElseThrow(() -> new NotFoundException(ErrorCode.OPTION_NOT_FOUND));

        option.update(request.type());

        return OptionResponse.from(option);
    }

    public void deleteOption(Long optionId){
        if(!optionRepository.existsById(optionId)){
            throw new NotFoundException(ErrorCode.OPTION_NOT_FOUND);
        }

        optionRepository.deleteById(optionId);
    }

}
