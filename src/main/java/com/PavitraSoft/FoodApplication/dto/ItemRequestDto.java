package com.PavitraSoft.FoodApplication.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;

@Data
public class ItemRequestDto {

    @NotNull
    private String restaurantId;

    @NotBlank
    private String name;

    @NotNull
    @Positive
    private Double price;

    @NotBlank
    private String category;
    private boolean veg;

    private String description;

    private String imageUrl;
}
