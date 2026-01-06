package com.PavitraSoft.FoodApplication.dto;

import com.PavitraSoft.FoodApplication.model.Address;
import lombok.Data;

import java.util.List;

@Data
public class RestaurantResponseDto {

    private String id;
    private String name;
    private String description;
    private Address address;
    private Double rating;
    private boolean isOpen;
    private String cuisine;
    private Long basePrice;

    private String phone;
    private List<String> categories;

    private String imageUrl;
    private boolean isActive;
}
