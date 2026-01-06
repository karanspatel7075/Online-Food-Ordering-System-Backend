package com.PavitraSoft.FoodApplication.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Data
public class AddressRequestDto {

    @NotBlank
    private String street;

    @NotBlank
    private String city;

    @NotBlank
    private String state;

    @Pattern(regexp = "\\d{6}", message = "Invalid pincode")
    private String pincode;

    private boolean isDefault;
}
