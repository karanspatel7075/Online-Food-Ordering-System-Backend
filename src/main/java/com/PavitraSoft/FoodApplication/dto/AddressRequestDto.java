package com.PavitraSoft.FoodApplication.dto;

import lombok.Data;

@Data
public class AddressRequestDto {
    private String street;
    private String city;
    private String state;
    private String pincode;
    private boolean isDefault;
}
