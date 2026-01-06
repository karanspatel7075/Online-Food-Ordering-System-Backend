package com.PavitraSoft.FoodApplication.dto;

import com.PavitraSoft.FoodApplication.enums.OrderStatus;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class OrderTimeLineDto {
    private OrderStatus status;
    private LocalDateTime time;
}
