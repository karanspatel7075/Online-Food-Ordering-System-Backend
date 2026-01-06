package com.PavitraSoft.FoodApplication.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CartItemResponseDto {
    private String menuItemId;
    private String name;
    private Double price;
    private Integer quantity;
    private Double totalPrice;
}
