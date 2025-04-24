package com.example.evostyle.domain.product.optiongroup.controller;

import com.example.evostyle.domain.product.optiongroup.dto.request.CreateOptionGroupRequest;
import com.example.evostyle.domain.product.optiongroup.dto.request.UpdateOptionGroupRequest;
import com.example.evostyle.domain.product.optiongroup.dto.response.CreateOptionGroupResponse;
import com.example.evostyle.domain.product.optiongroup.dto.response.OptionGroupResponse;
import com.example.evostyle.domain.product.optiongroup.service.OptionGroupService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/api")
@RestController
@RequiredArgsConstructor
public class OptionGroupController {

    private final OptionGroupService optionGroupService ;

    @PostMapping("/products/{productId}/optionGroup/options")
    public ResponseEntity<CreateOptionGroupResponse> createOptionGroup(@RequestBody CreateOptionGroupRequest request,
                                                                       @PathVariable(name = "productId")Long productId){

        CreateOptionGroupResponse response = optionGroupService.createOptionGroupWithOptions(request, productId);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PatchMapping("/optionGroups/{optionGroupId}")
    public ResponseEntity<OptionGroupResponse> updateOptionGroup(@RequestBody UpdateOptionGroupRequest request,
                                                                 @PathVariable(name = "optionGroupId") Long optionGroupId){

        OptionGroupResponse response = optionGroupService.updateOptionGroupName(request, optionGroupId);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }
}
