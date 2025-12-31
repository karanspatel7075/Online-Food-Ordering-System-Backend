package com.PavitraSoft.FoodApplication.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Cart {

    private String restaurantId;

    private List<OrderItem> items;

    private Double totalAmount;

    private LocalDateTime updatedAt;
}
