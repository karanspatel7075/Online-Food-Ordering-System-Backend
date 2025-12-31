package com.PavitraSoft.FoodApplication.controller;

import com.PavitraSoft.FoodApplication.dto.AddressRequestDto;
import com.PavitraSoft.FoodApplication.dto.AddressResponseDto;
import com.PavitraSoft.FoodApplication.dto.UpdateProfileRequestDto;
import com.PavitraSoft.FoodApplication.dto.UserProfileResponseDto;
import com.PavitraSoft.FoodApplication.model.User;
import com.PavitraSoft.FoodApplication.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    // 🔐 Get logged-in user profile
    @GetMapping("/me")
    public ResponseEntity<UserProfileResponseDto> getProfile(@AuthenticationPrincipal User user) {
        return ResponseEntity.ok(userService.getProfile(user));
    }

    // 🔐 Update logged-in user profile
    @PutMapping("/me")
    public ResponseEntity<UserProfileResponseDto> updateProfile(@AuthenticationPrincipal User user, @RequestBody UpdateProfileRequestDto dto) {
        return ResponseEntity.ok(userService.updateProfile(user, dto));
    }

    @PostMapping("/addresses")
    public AddressResponseDto addAddress(@AuthenticationPrincipal User user, @RequestBody AddressRequestDto dto) {
        return userService.addAddress(dto, user);
    }

    @GetMapping("/addresses")
    public List<AddressResponseDto> getAddresses(@AuthenticationPrincipal User user) {
        return userService.getAddresses(user);
    }

    @PutMapping("/addresses/{addressId}")
    public AddressResponseDto updateAddress(@PathVariable String addressId, @AuthenticationPrincipal User user, @RequestBody AddressRequestDto dto) {
        return userService.updateAddress(addressId, user, dto);
    }

    @DeleteMapping("/addresses/{addressId}")
    public String deleteAddress(@PathVariable String addressId, @AuthenticationPrincipal User user) {
        userService.deleteAddress(addressId, user);
        return "Address successfully deleted";
    }
}
