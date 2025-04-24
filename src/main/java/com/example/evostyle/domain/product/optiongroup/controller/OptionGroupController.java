package com.example.evostyle.domain.product.optiongroup.controller;

import com.example.evostyle.domain.product.optiongroup.dto.request.CreateOptionGroupRequest;
import com.example.evostyle.domain.product.optiongroup.dto.request.CreateOptionRequest;
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
    public ResponseEntity<OptionGroupResponse> createOptionGroup(@RequestBody CreateOptionGroupRequest request,
                                                                 @PathVariable(name = "productId")Long productId){

        OptionGroupResponse response = optionGroupService.createOptionGroupWithOptions(request, productId);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
}
