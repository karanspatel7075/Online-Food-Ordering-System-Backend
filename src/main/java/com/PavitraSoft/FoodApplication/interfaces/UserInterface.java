package com.PavitraSoft.FoodApplication.interfaces;

import com.PavitraSoft.FoodApplication.dto.AddressRequestDto;
import com.PavitraSoft.FoodApplication.dto.AddressResponseDto;
import com.PavitraSoft.FoodApplication.model.Address;
import com.PavitraSoft.FoodApplication.model.User;

import java.util.List;

public interface UserInterface {
    //
    AddressResponseDto addAddress(AddressRequestDto dto, User user);
    List<AddressResponseDto> getAddresses(User user);
    AddressResponseDto updateAddress(String addressId, User user, AddressRequestDto dto);
    void deleteAddress(String addressId, User user);
}
