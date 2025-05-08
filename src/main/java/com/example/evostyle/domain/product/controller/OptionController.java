package com.example.evostyle.domain.product.controller;


import com.example.evostyle.domain.product.dto.request.CreateOptionRequest;
import com.example.evostyle.domain.product.dto.request.UpdateOptionRequest;
import com.example.evostyle.domain.product.dto.response.OptionResponse;
import com.example.evostyle.domain.product.service.OptionService;
import com.example.evostyle.global.security.AuthUser;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class OptionController {

    public final OptionService optionService;

    @PostMapping("/option-groups/{optionGroupId}/options")
    public ResponseEntity<List<OptionResponse>> createOption(@PathVariable(name = "optionGroupId")Long optionGroupId,
                                                       @RequestBody List<CreateOptionRequest> requestList,
                                                       @AuthenticationPrincipal AuthUser authUser){

        List<OptionResponse> response = optionService.createOption(authUser.memberId(), optionGroupId, requestList);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("option-groups/{optionGroupId}/options")
    public ResponseEntity<List<OptionResponse>> readByOptionGroup(@PathVariable(name = "optionGroupId") Long optionGroupId) {

        List<OptionResponse> responseList = optionService.readByOptionGroup(optionGroupId);

        return ResponseEntity.status(HttpStatus.OK).body(responseList);
    }

    @PatchMapping("/options/{optionId}")
    public ResponseEntity<OptionResponse> updateOption(@RequestBody UpdateOptionRequest request,
                                                       @PathVariable Long optionId) {

        OptionResponse response = optionService.updateOption(request, optionId);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @DeleteMapping("/options/{optionId}")
    public ResponseEntity<Map<String, Long>> deleteOption(@PathVariable(name = "optionId") Long optionId) {

        optionService.deleteOption(optionId);
        return ResponseEntity.status(HttpStatus.OK).body(Map.of("optionId", optionId));
    }
}
