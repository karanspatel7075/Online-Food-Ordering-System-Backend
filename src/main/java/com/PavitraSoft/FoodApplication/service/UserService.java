package com.PavitraSoft.FoodApplication.service;

import com.PavitraSoft.FoodApplication.dto.AddressRequestDto;
import com.PavitraSoft.FoodApplication.dto.AddressResponseDto;
import com.PavitraSoft.FoodApplication.dto.UpdateProfileRequestDto;
import com.PavitraSoft.FoodApplication.dto.UserProfileResponseDto;
import com.PavitraSoft.FoodApplication.enums.Role;
import com.PavitraSoft.FoodApplication.globalController.BadRequestException;
import com.PavitraSoft.FoodApplication.globalController.ForbiddenException;
import com.PavitraSoft.FoodApplication.globalController.ResourceNotFoundException;
import com.PavitraSoft.FoodApplication.interfaces.UserInterface;
import com.PavitraSoft.FoodApplication.model.Address;
import com.PavitraSoft.FoodApplication.model.User;
import com.PavitraSoft.FoodApplication.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserService implements UserInterface {

    private final UserRepository userRepository;

    public User getUserByEmail(String email) {
        return userRepository.findByEmail(email).orElseThrow(() -> new ResourceNotFoundException("User not found"));
    }

    public UserProfileResponseDto getProfile(User user) {
        return UserProfileResponseDto.builder()
                .id(user.getId())
                .name(user.getName())
                .email(user.getEmail())
                .phone(user.getPhone())
                .role(user.getRole().name())
                .build();
    }

    public UserProfileResponseDto updateProfile(User user, UpdateProfileRequestDto dto) {
        user.setName(dto.getName());
        user.setPhone(dto.getPhone());
        userRepository.save(user);
        return getProfile(user);
    }

    @Override
    public AddressResponseDto addAddress(AddressRequestDto dto, User user) {

        Address address = Address.create(dto);

        // If first address → make default
        if(user.getAddress().isEmpty()) {
            address.setDefault(true);
        }

        // If new address is default → unset others
        if(address.isDefault()) {
            user.getAddress().forEach(a -> a.setDefault(false));
        }

        user.getAddress().add(address);
        userRepository.save(user);

        return mapToResponse(address);
    }

    @Override
    public List<AddressResponseDto> getAddresses(User user) {
        return user.getAddress()
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Override
    public AddressResponseDto updateAddress(String addressId, User user, AddressRequestDto dto) {

        Address address = user.getAddress()
                .stream()
                .filter(a -> a.getId().equals(addressId))
                .findFirst()
                .orElseThrow(() -> new ResourceNotFoundException("Address not found"));

        if(dto.isDefault()) {
            user.getAddress().forEach(a -> a.setDefault(false));
            address.setDefault(true);
        }

        address.setStreet(dto.getStreet());
        address.setCity(dto.getCity());
        address.setState(dto.getState());
        address.setPincode(dto.getPincode());

        userRepository.save(user);
        return mapToResponse(address);
    }

    @Override
    public void deleteAddress(String addressId, User user) {

        Address address = user.getAddress()
                .stream()
                .filter(a -> a.getId().equals(addressId))
                .findFirst()
                .orElseThrow(() -> new ResourceNotFoundException("Address not found"));

        boolean wasDefault = address.isDefault();
        user.getAddress().remove(address);

        //If default deleted -> assign new default
        if(wasDefault && !user.getAddress().isEmpty()) {
            user.getAddress().get(0).setDefault(true);
        }

        userRepository.save(user);
    }

    private AddressResponseDto mapToResponse(Address address) {
        return AddressResponseDto.builder()
                .id(address.getId())
                .street(address.getStreet())
                .city(address.getCity())
                .state(address.getState())
                .pincode(address.getPincode())
                .isDefault(address.isDefault())
                .build();
    }

//    User applies to become Restaurant Admin
    public void applyForRestaurantAdmin(User user, String restaurantName) {
        if(user.getRole() != Role.USER) {
            throw new ForbiddenException("Only regular users can apply");
        }

        if(user.isPendingRestaurantAdminRequest()) {
            throw new BadRequestException("You already have a pending application.");
        }

        user.setPendingRestaurantAdminRequest(true); // flag to track request
        userRepository.save(user);
    }
}
