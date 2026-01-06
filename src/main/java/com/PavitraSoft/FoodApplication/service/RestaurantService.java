package com.PavitraSoft.FoodApplication.service;

import com.PavitraSoft.FoodApplication.dto.ItemRequestDto;
import com.PavitraSoft.FoodApplication.dto.RestaurantRequestDto;
import com.PavitraSoft.FoodApplication.dto.RestaurantResponseDto;
import com.PavitraSoft.FoodApplication.enums.RestaurantStatus;
import com.PavitraSoft.FoodApplication.globalController.ResourceNotFoundException;
import com.PavitraSoft.FoodApplication.model.MenuItem;
import com.PavitraSoft.FoodApplication.model.Restaurant;
import com.PavitraSoft.FoodApplication.model.User;
import com.PavitraSoft.FoodApplication.repository.MenuItemRepository;
import com.PavitraSoft.FoodApplication.repository.RestaurantRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.awt.event.ItemEvent;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class RestaurantService {

    private final RestaurantRepository restaurantRepository;

    public RestaurantService(RestaurantRepository restaurantRepository, MenuItemRepository menuItemRepository) {
        this.restaurantRepository = restaurantRepository;
    }

    public RestaurantResponseDto create(RestaurantRequestDto dto, User owner) {
        Restaurant restaurant = Restaurant.builder()
                .ownerId(owner.getId())
                .name(dto.getName())
                .address(dto.getAddress())
                .description(dto.getDescription())
                .phone(dto.getPhone())
                .cuisine(dto.getCuisine())
                .basePrice(dto.getBasePrice())
                .categories(dto.getCategories())
                .imageUrl(dto.getImageUrl())
                .rating(0.0)
                .isOpen(false)  // Restaurant is NOT visible to users until ADMIN approves.
                .active(false)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .status(RestaurantStatus.PENDING)
                .build();

        Restaurant savedRestaurant = restaurantRepository.save(restaurant);
        System.out.println("OwnerId " + owner.getId());

        return mapToDto(savedRestaurant);
    }

        public List<RestaurantResponseDto> getAll(String search, int page, int size) {
        //  First we were returning restaurants directly, but now we are returning with pagination;
            PageRequest pageRequest = PageRequest.of(page, size, Sort.by("rating").descending());
            Page<Restaurant> restaurants;

            if(search == null || search.isBlank()) {
                restaurants = restaurantRepository.findByActiveTrue(pageRequest);
            }
            else {
                restaurants = restaurantRepository.findByActiveTrueAndNameContainingIgnoreCaseOrActiveTrueAndCategoriesContainingIgnoreCase(search ,search,
                        pageRequest);
            }

            return restaurants
                    .getContent()
                    .stream()
                    .map(this::mapToDto)
                    .toList();
        }

    public RestaurantResponseDto getById(String id) {
        Restaurant restaurant = restaurantRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Restaurant not found"));
        return mapToDto(restaurant);
    }

    public RestaurantResponseDto mapToDto(Restaurant restaurant) {
        RestaurantResponseDto dto = new RestaurantResponseDto();
        dto.setId(restaurant.getId());
        dto.setName(restaurant.getName());
        dto.setAddress(restaurant.getAddress());
        dto.setDescription(restaurant.getDescription());
        dto.setActive(restaurant.isActive());
        dto.setCategories(restaurant.getCategories());
        dto.setCuisine(restaurant.getCuisine());
        dto.setOpen(restaurant.isOpen());
        dto.setPhone(restaurant.getPhone());
        dto.setRating(restaurant.getRating());
        dto.setBasePrice(restaurant.getBasePrice());
        dto.setImageUrl(restaurant.getImageUrl());

        return dto;
    }

}
