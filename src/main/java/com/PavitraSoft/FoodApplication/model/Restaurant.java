package com.PavitraSoft.FoodApplication.model;

import com.PavitraSoft.FoodApplication.enums.RestaurantStatus;
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
@Document(collection = "restaurants")
public class Restaurant {

    @Id
    private String id;
    private String ownerId;
    private String name;
    private String description;
    private Address address;
    private Double rating;
    private long ratingCount;
    private boolean isOpen;
    private String cuisine;
    private Long basePrice;

    private String phone;
    private List<String> categories;

    private String imageUrl;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private boolean active;

    private RestaurantStatus status;

}
