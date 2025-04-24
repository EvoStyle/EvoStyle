package com.example.evostyle.domain.product.optiongroup.controller;

import com.example.evostyle.domain.product.optiongroup.dto.request.OptionGroupRequest;
import com.example.evostyle.domain.product.optiongroup.dto.response.OptionGroupResponse;
import com.example.evostyle.domain.product.optiongroup.service.OptionGroupService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequiredArgsConstructor
@RequestMapping("/api/optionGroups")
public class OptionGroupController {

    private final OptionGroupService optionGroupService ;

    @PostMapping
    public ResponseEntity<OptionGroupResponse> createOptionGroup(@RequestBody OptionGroupRequest request){
       OptionGroupResponse response = optionGroupService.createOptionGroup(request);

       return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @GetMapping
    public ResponseEntity<List<OptionGroupResponse>> readAllOptionGroup(){
        List<OptionGroupResponse> responseList = optionGroupService.readAllOptionGroup();

        return ResponseEntity.status(HttpStatus.OK).body(responseList);
    }
}
