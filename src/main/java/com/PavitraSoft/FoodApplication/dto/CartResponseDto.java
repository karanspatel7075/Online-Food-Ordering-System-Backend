package com.PavitraSoft.FoodApplication.dto;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
public class CartResponseDto {
    private String cartId;
    private String restaurantId;
    private List<CartItemResponseDto> items;
    private Double totalAmount;
    private LocalDateTime updatedAt;
}
