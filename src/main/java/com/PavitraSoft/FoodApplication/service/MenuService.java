package com.PavitraSoft.FoodApplication.service;

import com.PavitraSoft.FoodApplication.dto.ItemRequestDto;
import com.PavitraSoft.FoodApplication.enums.RestaurantStatus;
import com.PavitraSoft.FoodApplication.enums.Role;
import com.PavitraSoft.FoodApplication.globalController.BadRequestException;
import com.PavitraSoft.FoodApplication.globalController.ForbiddenException;
import com.PavitraSoft.FoodApplication.globalController.ResourceNotFoundException;
import com.PavitraSoft.FoodApplication.model.MenuItem;
import com.PavitraSoft.FoodApplication.model.Restaurant;
import com.PavitraSoft.FoodApplication.model.User;
import com.PavitraSoft.FoodApplication.repository.MenuItemRepository;
import com.PavitraSoft.FoodApplication.repository.RestaurantRepository;
import com.PavitraSoft.FoodApplication.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class MenuService {

    private final MenuItemRepository itemRepository;
    private final RestaurantRepository restaurantRepository;

    public MenuService(MenuItemRepository itemRepository, RestaurantRepository restaurantRepository, UserRepository userRepository) {
        this.itemRepository = itemRepository;
        this.restaurantRepository = restaurantRepository;
    }

    private static final Logger log = LoggerFactory.getLogger(MenuService.class);

    //USER
    public List<MenuItem> getMenuForRestaurant(String restaurantId) {
        log.info("Fetching menu for restaurantId={}", restaurantId);
        Restaurant restaurant = restaurantRepository.findById(restaurantId).orElseThrow(() -> new ResourceNotFoundException("Restaurant not found"));

        if(!restaurant.isActive() || !restaurant.isOpen()) {
            throw  new BadRequestException("Restaurant not available");
        }

        if (restaurant.getStatus() != RestaurantStatus.APPROVED) {
            throw new ForbiddenException("Restaurant not approved by admin");
        }

        return itemRepository.findByRestaurantId(restaurantId);
    }

    //Restaurant_ADMIN
    public void addItem(User admin, ItemRequestDto dto) {
        log.info("Add menu item request received");
        log.debug("Admin ID: {}, Restaurant ID: {}", admin.getId(), dto.getRestaurantId());
        Restaurant restaurant = restaurantRepository.findById(dto.getRestaurantId()).orElseThrow(() -> new ResourceNotFoundException("Restaurant not found"));

        if(admin.getRole() != Role.RESTAURANT_ADMIN) {
            throw new ForbiddenException("You do not have permission to manage this restaurant.");
        }

        // Ownership check (CRITICAL)
        // This part is preventing to add item
        if(!restaurant.getOwnerId().equals(admin.getId())) {
            throw new ForbiddenException("You are not owner of this restaurant");
        }

        if (restaurant.getStatus() != RestaurantStatus.APPROVED) {
            throw new ForbiddenException("Restaurant not approved by admin");
        }

        MenuItem item = MenuItem.builder()
                .restaurantId(dto.getRestaurantId())
                .name(dto.getName())
                .description(dto.getDescription())
                .price(dto.getPrice())
                .veg(dto.isVeg())
                .category(dto.getCategory())
                .imageUrl(dto.getImageUrl())
                .isAvailable(true)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        MenuItem savedItem = itemRepository.save(item);
        System.out.println("SAVED ITEM ID = " + savedItem.getId());
    }

    public MenuItem updateItem(User admin, ItemRequestDto dto, String restaurantId, String itemId) {
        Restaurant restaurant = restaurantRepository.findById(restaurantId).orElseThrow(() -> new ResourceNotFoundException("Restaurant Not found"));
        System.out.println("ADD ITEM CALLED");
        System.out.println("Admin ID: " + admin.getId());
        System.out.println("Restaurant ID: " + dto.getRestaurantId());

        // This part is preventing to update item
        if(!restaurant.getOwnerId().equals(admin.getId())) {
            throw new ForbiddenException("Not authorized Admin");
        }

        if (restaurant.getStatus() != RestaurantStatus.APPROVED) {
            throw new ForbiddenException("Restaurant not approved by admin");
        }

        MenuItem item = itemRepository.findById(itemId).orElseThrow(() -> new ResourceNotFoundException("Item not found"));

        item.setName(dto.getName());
        item.setPrice(dto.getPrice());
        item.setCategory(dto.getCategory());
        item.setVeg(dto.isVeg());
        item.setDescription(dto.getDescription());
        item.setImageUrl(dto.getImageUrl());
        item.setUpdatedAt(LocalDateTime.now());

        System.out.println("Updated ITEM ID = " + item.getId());
        return itemRepository.save(item);
    }

    public void deleteItem(User admin, String restaurantId, String itemId) {

        Restaurant restaurant = restaurantRepository.findById(restaurantId).orElseThrow(() -> new ResourceNotFoundException("Restaurant not found"));

        if (!restaurant.getOwnerId().equals(admin.getId())) {
            throw new ForbiddenException("Not authorized");
        }

        if (restaurant.getStatus() != RestaurantStatus.APPROVED) {
            throw new ForbiddenException("Restaurant not approved by admin");
        }

        MenuItem item = itemRepository.findById(itemId).orElseThrow(()  -> new ResourceNotFoundException("Item not found"));

        item.setAvailable(false);
        item.setUpdatedAt(LocalDateTime.now());
        itemRepository.save(item);
    }
}
