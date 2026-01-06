package com.PavitraSoft.FoodApplication.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.List;

@Data
public class CreateOrderRequestDto {

    @NotNull
    private String restaurantId;

    @NotEmpty
    private List<OrderItemRequestDto> items;
}
