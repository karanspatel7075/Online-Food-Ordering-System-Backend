package com.PavitraSoft.FoodApplication.model;

import com.PavitraSoft.FoodApplication.dto.AddressRequestDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Address {

    private String id;
    private String street;
    private String city;
    private String state;
    private String pincode;
    private boolean isDefault;

    public static Address create(AddressRequestDto dto) {
        return Address.builder()
                .id(UUID.randomUUID().toString())
                .street(dto.getStreet())
                .city(dto.getCity())
                .state(dto.getState())
                .pincode(dto.getPincode())
                .isDefault(dto.isDefault())
                .build();
    }
}
