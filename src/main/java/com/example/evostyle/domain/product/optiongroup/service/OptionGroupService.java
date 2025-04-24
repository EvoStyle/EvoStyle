package com.example.evostyle.domain.product.optiongroup.service;

import com.example.evostyle.domain.product.optiongroup.dto.request.OptionGroupRequest;
import com.example.evostyle.domain.product.optiongroup.dto.response.OptionGroupResponse;
import com.example.evostyle.domain.product.optiongroup.entity.OptionGroup;
import com.example.evostyle.domain.product.optiongroup.repository.OptionGroupRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class OptionGroupService {

    private final OptionGroupRepository optionGroupRepository ;

    //관리자
    public OptionGroupResponse createOptionGroup(OptionGroupRequest request){

      OptionGroup optionGroup = optionGroupRepository.save(OptionGroup.of(request.name()));

      return OptionGroupResponse.from(optionGroup);
    }

    public List<OptionGroupResponse>  readAllOptionGroup(){
        return optionGroupRepository.findAll().stream().map(OptionGroupResponse::from).toList();
    }
}
