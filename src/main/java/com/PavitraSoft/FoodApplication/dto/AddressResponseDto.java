package com.PavitraSoft.FoodApplication.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AddressResponseDto {

    private String id;
    private String street;
    private String city;
    private String state;
    private String pincode;
    private boolean isDefault;
}
