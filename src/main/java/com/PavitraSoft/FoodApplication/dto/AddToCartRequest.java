package com.PavitraSoft.FoodApplication.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;
import lombok.NonNull;

@Data
public class AddToCartRequest {

    @NotBlank
    private String restaurantId;

    @NotBlank
    private String menuItemId;

    @NotNull
    @Positive
    private Integer quantity;
}
