package com.example.evostyle.domain.brand.dto.response;

import com.example.evostyle.domain.brand.entity.Brand;

public record ReadBrandResponse(Long id, String name) {

    public static ReadBrandResponse from(Brand brand) {
        return new ReadBrandResponse(
                brand.getId(),
                brand.getName()
        );
    }
}
