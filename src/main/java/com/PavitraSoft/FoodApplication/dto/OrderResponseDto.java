package com.PavitraSoft.FoodApplication.dto;

import com.PavitraSoft.FoodApplication.enums.OrderStatus;
import com.PavitraSoft.FoodApplication.model.Order;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Data
@Builder
public class OrderResponseDto {
    private String orderId;
    private String restaurantId;
    private List<OrderItemResponseDto> items;

    private Double totalAmount;
    private OrderStatus status;

    private LocalDateTime createdAt;
    private List<OrderTimeLineDto> timeline;

    private LocalDateTime estimatedDeliveryTime;
}
