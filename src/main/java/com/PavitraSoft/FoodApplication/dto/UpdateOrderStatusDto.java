package com.PavitraSoft.FoodApplication.dto;

import com.PavitraSoft.FoodApplication.enums.OrderStatus;
import lombok.Data;

@Data
public class UpdateOrderStatusDto {
    private OrderStatus status;
}
