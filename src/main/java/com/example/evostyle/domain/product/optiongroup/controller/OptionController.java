package com.example.evostyle.domain.product.optiongroup.controller;


import com.example.evostyle.domain.product.optiongroup.dto.request.UpdateOptionRequest;
import com.example.evostyle.domain.product.optiongroup.dto.response.OptionResponse;
import com.example.evostyle.domain.product.optiongroup.service.OptionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class OptionController {

    public final OptionService optionService;


    @GetMapping("optionGroups/{optionGroupId}/options")
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
