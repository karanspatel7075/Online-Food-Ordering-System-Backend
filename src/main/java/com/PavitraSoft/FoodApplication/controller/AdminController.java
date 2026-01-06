package com.PavitraSoft.FoodApplication.controller;

import com.PavitraSoft.FoodApplication.enums.RestaurantStatus;
import com.PavitraSoft.FoodApplication.enums.Role;
import com.PavitraSoft.FoodApplication.globalController.BadRequestException;
import com.PavitraSoft.FoodApplication.globalController.ForbiddenException;
import com.PavitraSoft.FoodApplication.globalController.ResourceNotFoundException;
import com.PavitraSoft.FoodApplication.model.Restaurant;
import com.PavitraSoft.FoodApplication.model.User;
import com.PavitraSoft.FoodApplication.repository.RestaurantRepository;
import com.PavitraSoft.FoodApplication.repository.UserRepository;
import com.PavitraSoft.FoodApplication.service.AdminService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/admin")
@RequiredArgsConstructor
public class AdminController {

    private final AdminService adminService;
    private final UserRepository userRepository;
    private final RestaurantRepository restaurantRepository;

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/pending-restaurant-admin")
    public List<User> getPendingAdminRequests() {
        return userRepository.findByPendingRestaurantAdminRequestTrue();
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/approve-restaurant-admin/{userId}")
    public ResponseEntity<String> approve(@PathVariable String userId) {
        adminService.approveRestaurantAdmin(userId);
        return ResponseEntity.ok("User is promoted to Restaurant Admin");
    }

    //    View Pending Restaurants
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/restaurants/pending")
    public List<Restaurant> getPendingRestaurants() {
        return restaurantRepository.findByStatus(RestaurantStatus.PENDING);
    }

    //    Approve Restaurant
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/restaurants/{restaurantId}/approve")
    public ResponseEntity<String> approveRestaurant(@PathVariable String restaurantId) {

        Restaurant restaurant = restaurantRepository.findById(restaurantId).orElseThrow(() -> new ResourceNotFoundException("Restaurant not found"));

        restaurant.setStatus(RestaurantStatus.APPROVED);
        restaurant.setActive(true);
        restaurant.setOpen(true);
        restaurant.setUpdatedAt(LocalDateTime.now());

        restaurantRepository.save(restaurant);

        return ResponseEntity.ok("Restaurant approved");
    }

    //    Reject Restaurant
    @PostMapping("/restaurants/{restaurantId}/reject")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<String> rejectRestaurant(@PathVariable String restaurantId, @RequestParam String reason) {
        Restaurant restaurant = restaurantRepository.findById(restaurantId).orElseThrow(() -> new ResourceNotFoundException("Restaurant not found"));

        restaurant.setStatus(RestaurantStatus.REJECTED);
        restaurant.setActive(false);
        restaurant.setUpdatedAt(LocalDateTime.now());

        restaurantRepository.save(restaurant);

        return ResponseEntity.ok("Restaurant rejected : " + reason);
    }

    //    Block / Unblock Restaurant (CRITICAL)
    @PostMapping("/restaurants/{restaurantId}/block")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<String> blockRestaurant(@PathVariable String restaurantId) {
        Restaurant restaurant = restaurantRepository.findById(restaurantId).orElseThrow(() -> new ResourceNotFoundException("Restaurant not found"));

        restaurant.setStatus(RestaurantStatus.BLOCKED);
        restaurant.setActive(false);
        restaurant.setOpen(false);

        restaurantRepository.save(restaurant);

        return ResponseEntity.ok("Restaurant blocked");
    }

    @PostMapping("/restaurants/{restaurantId}/unblock")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<String> unblockRestaurant(@PathVariable String restaurantId) {
        Restaurant restaurant = restaurantRepository.findById(restaurantId).orElseThrow(() -> new ResourceNotFoundException("Restaurant not found"));

        restaurant.setStatus(RestaurantStatus.APPROVED);
        restaurant.setActive(true);
        restaurant.setOpen(false); // Doubt here will debug at testing

        restaurantRepository.save(restaurant);

        return ResponseEntity.ok("Restaurant unblocked");
    }

    //    Ownership Transfer
    @PostMapping("/restaurants/{restaurantId}/transfer-owner/{userId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<String> transferOwnership(@PathVariable String restaurantId, @PathVariable String userId) {
        Restaurant restaurant = restaurantRepository.findById(restaurantId).orElseThrow(() -> new ResourceNotFoundException("Restaurant not found"));

        User user = userRepository.findById(userId).orElseThrow(() -> new ResourceNotFoundException("User not found"));

        if (user.getRole() != Role.RESTAURANT_ADMIN) {
            throw new BadRequestException("User is not Restaurant Admin");
        }

        restaurant.setOwnerId(user.getId());
        restaurantRepository.save(restaurant);

        return ResponseEntity.ok("Ownership transferred");


    }
}