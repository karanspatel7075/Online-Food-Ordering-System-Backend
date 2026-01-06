package com.PavitraSoft.FoodApplication.service;

import com.PavitraSoft.FoodApplication.enums.OrderStatus;
import com.PavitraSoft.FoodApplication.enums.Role;
import com.PavitraSoft.FoodApplication.globalController.BadRequestException;
import com.PavitraSoft.FoodApplication.globalController.ConflictException;
import com.PavitraSoft.FoodApplication.globalController.ForbiddenException;
import com.PavitraSoft.FoodApplication.globalController.ResourceNotFoundException;
import com.PavitraSoft.FoodApplication.model.Order;
import com.PavitraSoft.FoodApplication.model.Restaurant;
import com.PavitraSoft.FoodApplication.model.User;
import com.PavitraSoft.FoodApplication.repository.OrderRepository;
import com.PavitraSoft.FoodApplication.repository.RestaurantRepository;
import com.PavitraSoft.FoodApplication.repository.UserRepository;
import com.PavitraSoft.FoodApplication.util.OrderStateMachine;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
public class AdminService {

    private final UserRepository userRepository;
    private final OrderRepository orderRepository;
    private final RestaurantRepository restaurantRepository;

    public AdminService(UserRepository userRepository, OrderRepository orderRepository, RestaurantRepository restaurantRepository) {
        this.userRepository = userRepository;
        this.orderRepository = orderRepository;
        this.restaurantRepository = restaurantRepository;
    }

    @Transactional
    public void approveRestaurantAdmin(String userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new ResourceNotFoundException("User not found"));

        if(!user.isPendingRestaurantAdminRequest()) {
            throw new BadRequestException("No pending request found for this user");
        }

        user.setRole(Role.RESTAURANT_ADMIN);
        user.setPendingRestaurantAdminRequest(false);
        userRepository.save(user);
    }

    public Order adminCancelOrder(User admin, String orderId) {

        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new ResourceNotFoundException("Order not found"));

        Restaurant restaurant = restaurantRepository.findById(order.getRestaurantId())
                .orElseThrow(() -> new ResourceNotFoundException("Restaurant not found"));

        if (!restaurant.getOwnerId().equals(admin.getId())) {
            throw new ForbiddenException("Not authorized");
        }

        if (!OrderStateMachine.canTransition(order.getStatus(), OrderStatus.CANCELLED)) {
            throw new ConflictException("Order cannot be cancelled now");
        }

        order.setStatus(OrderStatus.CANCELLED);
        order.setUpdatedAt(LocalDateTime.now());

        return orderRepository.save(order);
    }

}
