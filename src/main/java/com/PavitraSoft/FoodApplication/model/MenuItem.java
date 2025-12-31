package com.PavitraSoft.FoodApplication.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Document(collection = "menu_items")
public class MenuItem {

    @Id
    private String id;
    private String restaurantId;
    private String name;
    private Double price;
    private String category;
    private boolean veg;
    private String description;

    private String imageUrl;
    private boolean isAvailable;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
