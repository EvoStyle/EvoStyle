package com.example.evostyle.domain.product.optiongroup.controller;

import com.example.evostyle.domain.product.optiongroup.dto.request.CreateOptionGroupRequest;
import com.example.evostyle.domain.product.optiongroup.dto.request.UpdateOptionGroupRequest;
import com.example.evostyle.domain.product.optiongroup.dto.response.CreateOptionGroupResponse;
import com.example.evostyle.domain.product.optiongroup.dto.response.OptionGroupResponse;
import com.example.evostyle.domain.product.optiongroup.service.OptionGroupService;
import com.example.evostyle.domain.product.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;


@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class OptionGroupController {

    private final OptionGroupService optionGroupService;

    @PostMapping("/products/{productId}/optionGroups")
    public ResponseEntity<CreateOptionGroupResponse> createOptionGroup(@RequestBody CreateOptionGroupRequest request,
                                                                       @PathVariable(name = "productId") Long productId) {

        CreateOptionGroupResponse response = optionGroupService.createOptionGroupWithOptions(request, productId);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/products/{productId}/optionGroups")
    public ResponseEntity<List<OptionGroupResponse>> readOptionGroupByProduct(@PathVariable(name = "productId") Long productId) {

        List<OptionGroupResponse> responseList = optionGroupService.readOptionGroupByProduct(productId);
        return ResponseEntity.status(HttpStatus.OK).body(responseList);
    }

    @PatchMapping("/optionGroups/{optionGroupId}")
    public ResponseEntity<OptionGroupResponse> updateOptionGroup(@RequestBody UpdateOptionGroupRequest request,
                                                                 @PathVariable(name = "optionGroupId") Long optionGroupId) {

        OptionGroupResponse response = optionGroupService.updateOptionGroupName(request, optionGroupId);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @DeleteMapping("/optionGroups/{optionGroupId}")
    public ResponseEntity<Map<String, Long>> deleteOptionGroup(@PathVariable(name = "optionGroupId") Long optionGroupId) {

        optionGroupService.deleteOptionGroup(optionGroupId);
        return ResponseEntity.status(HttpStatus.OK).body(Map.of("optionGroupId", optionGroupId));
    }
}
