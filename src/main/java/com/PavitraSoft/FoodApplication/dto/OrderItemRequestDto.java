package com.PavitraSoft.FoodApplication.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;
import lombok.NonNull;

@Data
public class OrderItemRequestDto {

    @NotNull
    private String menuItemId;

    @NotNull
    @Positive
    private Integer quantity;
}
