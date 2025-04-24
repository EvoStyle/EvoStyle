package com.example.evostyle.domain.brand.dto.response;

import com.example.evostyle.domain.brand.entity.Brand;

public record CreateBrandResponse(Long id, String name) {

    public static CreateBrandResponse of(Brand brand) {
        return new CreateBrandResponse(
                brand.getId(),
                brand.getName()
        );
    }
}