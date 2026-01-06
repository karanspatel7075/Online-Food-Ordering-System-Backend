package com.PavitraSoft.FoodApplication.dto;

import com.PavitraSoft.FoodApplication.model.Address;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Positive;
import lombok.Data;
import lombok.NonNull;

import java.util.List;

@Data
public class RestaurantRequestDto {

    @NotBlank(message = "Restaurant name is required")
    private String name;
    private String description;

    private Address address;

    @NotBlank
    private String cuisine;

    @NotNull
    @Positive
    private Long basePrice;

    @Pattern(regexp = "^[6-9]\\d{9}$", message = "Invalid phone number")
    private String phone;
    private List<String> categories;
    private String imageUrl;
}
