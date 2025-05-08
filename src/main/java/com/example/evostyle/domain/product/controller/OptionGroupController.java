package com.example.evostyle.domain.product.controller;

import com.example.evostyle.domain.product.dto.request.CreateOptionGroupRequest;
import com.example.evostyle.domain.product.dto.request.UpdateOptionGroupRequest;
import com.example.evostyle.domain.product.dto.response.CreateOptionGroupResponse;
import com.example.evostyle.domain.product.dto.response.OptionGroupResponse;
import com.example.evostyle.domain.product.service.OptionGroupService;
import com.example.evostyle.global.security.AuthUser;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;


@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class OptionGroupController {

    private final OptionGroupService optionGroupService;

    @PostMapping("/products/{productId}/option-groups")
    public ResponseEntity<CreateOptionGroupResponse> createOptionGroup(@RequestBody CreateOptionGroupRequest request,
                                                                       @PathVariable(name = "productId") Long productId,
                                                                       @AuthenticationPrincipal AuthUser authUser) {
        Long memberId = authUser.memberId();
        CreateOptionGroupResponse response = optionGroupService.createOptionGroupWithOptions(memberId, productId, request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/products/{productId}/option-groups")
    public ResponseEntity<List<OptionGroupResponse>> readOptionGroupByProduct(@PathVariable(name = "productId") Long productId) {

        List<OptionGroupResponse> responseList = optionGroupService.readOptionGroupByProduct(productId);
        return ResponseEntity.status(HttpStatus.OK).body(responseList);
    }

    @PatchMapping("/option-groups/{optionGroupId}")
    public ResponseEntity<OptionGroupResponse> updateOptionGroup(@RequestBody UpdateOptionGroupRequest request,
                                                                 @PathVariable(name = "optionGroupId") Long optionGroupId,
                                                                 @AuthenticationPrincipal AuthUser authUser) {

        OptionGroupResponse response = optionGroupService.updateOptionGroupName(authUser.memberId(), request, optionGroupId);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @DeleteMapping("/option-groups/{optionGroupId}")
    public ResponseEntity<Map<String, Long>> deleteOptionGroup(@PathVariable(name = "optionGroupId") Long optionGroupId,
                                                               @AuthenticationPrincipal AuthUser authUser) {

        optionGroupService.deleteOptionGroup(authUser.memberId(), optionGroupId);
        return ResponseEntity.status(HttpStatus.OK).body(Map.of("optionGroupId", optionGroupId));
    }
}
