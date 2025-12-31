package com.PavitraSoft.FoodApplication.model;

import com.PavitraSoft.FoodApplication.enums.OrderStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Document(collection = "orders")
public class Order {

    @Id
    private String id;
    private String restaurantId;
    private String userId;
    private List<OrderItem> items;
    private Double totalAmount;
    private OrderStatus status;
    private PaymentDetails payment;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
