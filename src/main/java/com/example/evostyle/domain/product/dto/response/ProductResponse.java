package com.example.evostyle.domain.product.dto.response;

import com.example.evostyle.domain.product.entity.Product;

import java.time.LocalDateTime;

public record ProductResponse(
        Long id,
        Long brandId,
        String name,
        String description,
        Integer price,
        Integer likeCount,
        Float averageRating,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {

    public static ProductResponse from(Product product){
        return new ProductResponse(product.getId(),
                                   product.getBrand().getId(),
                                   product.getName(),
                                   product.getDescription(),
                                   product.getPrice(),
                                   product.getLikeCount(),
                                   product.getAverageRating(),
                                   product.getCreatedAt(),
                                   product.getUpdatedAt());
    }
}
