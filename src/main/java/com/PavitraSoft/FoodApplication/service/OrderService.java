package com.PavitraSoft.FoodApplication.service;

import com.PavitraSoft.FoodApplication.dto.*;
import com.PavitraSoft.FoodApplication.enums.OrderStatus;
import com.PavitraSoft.FoodApplication.globalController.BadRequestException;
import com.PavitraSoft.FoodApplication.globalController.ConflictException;
import com.PavitraSoft.FoodApplication.globalController.ForbiddenException;
import com.PavitraSoft.FoodApplication.globalController.ResourceNotFoundException;
import com.PavitraSoft.FoodApplication.model.*;
import com.PavitraSoft.FoodApplication.repository.MenuItemRepository;
import com.PavitraSoft.FoodApplication.repository.OrderRepository;
import com.PavitraSoft.FoodApplication.repository.RestaurantRepository;
import com.PavitraSoft.FoodApplication.util.OrderStateMachine;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class OrderService {

    private final MenuItemRepository itemRepository;
    private final OrderRepository orderRepository;
    private final RestaurantRepository restaurantRepository;
    private final OrderStateMachine orderStateMachine;

    public OrderService(MenuItemRepository itemRepository, OrderRepository orderRepository, RestaurantRepository restaurantRepository, OrderStateMachine orderStateMachine) {
        this.itemRepository = itemRepository;
        this.orderRepository = orderRepository;
        this.restaurantRepository = restaurantRepository;
        this.orderStateMachine = orderStateMachine;
    }

    public OrderResponseDto  createOrder(User user, CreateOrderRequestDto dto) {
        double total= 0;
        List<OrderItem> orderItems = new ArrayList<>();

        for(OrderItemRequestDto req : dto.getItems()) {
            MenuItem item = itemRepository.findById(req.getMenuItemId()).orElseThrow(() -> new ResourceNotFoundException("Menu item not found"));

            if(!item.isAvailable()) {
                throw new BadRequestException("Item not available");
            }

            total += item.getPrice() * req.getQuantity();

            orderItems.add(OrderItem.builder()
                            .menuItemId(item.getId())
                            .name(item.getName())
                            .price(item.getPrice())
                            .quantity(req.getQuantity())
                            .build());
        }

        Order order = Order.builder()
                .userId(user.getId())
                .restaurantId(dto.getRestaurantId())
                .items(orderItems)
                .totalAmount(total)
                .status(OrderStatus.PLACED)
                .createdAt(LocalDateTime.now())
                .build();

        order.getStatusTimeLine().put(OrderStatus.PLACED, LocalDateTime.now());

        order.setEstimatedDeliveryTime(LocalDateTime.now().plusMinutes(45));

        return mapToOrderResponse(orderRepository.save(order));
    }

    private OrderResponseDto mapToOrderResponse(Order order) {
        return OrderResponseDto.builder()
                .orderId(order.getId())
                .restaurantId(order.getRestaurantId())
                .items(
                        order.getItems().stream()
                                .map(item -> OrderItemResponseDto.builder()
                                        .menuItemId(item.getMenuItemId())
                                        .name(item.getName())
                                        .price(item.getPrice())
                                        .quantity(item.getQuantity())
                                        .totalPrice(item.getPrice() * item.getQuantity())
                                        .build())
                                .toList()
                )
                .totalAmount(order.getTotalAmount())
                .status(order.getStatus())
                .createdAt(order.getCreatedAt())
                .timeline(buildTimeLine(order))
                .estimatedDeliveryTime(order.getEstimatedDeliveryTime())
                .build();
    }
    private List<OrderTimeLineDto> buildTimeLine(Order order) {
        return order.getStatusTimeLine()
                .entrySet()
                .stream()
                .sorted(Map.Entry.comparingByValue())
                .map(e -> OrderTimeLineDto.builder()
                        .status(e.getKey())
                        .time(e.getValue())
                        .build())
                .toList();
    }

    public List<OrderResponseDto> getMyOrder(User user) {
        return orderRepository.findByUserId(user.getId())
                .stream()
                .map(this::mapToOrderResponse)
                .toList();
    }

    public Order updateStatus(User admin, String orderId, OrderStatus status) {
        Order order = orderRepository.findById(orderId).orElseThrow(() -> new ResourceNotFoundException("Order not found"));

        Restaurant restaurant = restaurantRepository.findById(order.getRestaurantId()).orElseThrow(() -> new ResourceNotFoundException("Restaurant not found"));

        if (restaurant.getOwnerId() == null ||
                !restaurant.getOwnerId().equals(admin.getId())) {
            throw new ForbiddenException("Not authorized to manage this restaurant");
        }

        if (!OrderStateMachine.canTransition(order.getStatus(), status)) {
            throw new BadRequestException(
                    "Invalid order status transition from " + order.getStatus() + " to " + status
            );
        }

//        order.setStatus(status);
//        order.setUpdatedAt(LocalDateTime.now());

        order.markStatus(status);

        switch (status) {
            case ACCEPTED -> order.setEstimatedDeliveryTime(LocalDateTime.now().plusMinutes(40));
            case PREPARING -> order.setEstimatedDeliveryTime(LocalDateTime.now().plusMinutes(30));
            case OUT_FOR_DELIVERY -> order.setEstimatedDeliveryTime(LocalDateTime.now().plusMinutes(15));
        }

        return orderRepository.save(order);
    }

    public Order cancelOrderByUser(User user, String orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new ResourceNotFoundException("Order not found"));

        if (!order.getUserId().equals(user.getId())) {
            throw new ForbiddenException("You can cancel only your own order");
        }

        if (!OrderStateMachine.canTransition(order.getStatus(), OrderStatus.CANCELLED)) {
            throw new ConflictException("Order cannot be cancelled at this stage");
        }

//        order.setStatus(OrderStatus.CANCELLED);
        order.markStatus(OrderStatus.CANCELLED);
        order.setUpdatedAt(LocalDateTime.now());
        return orderRepository.save(order);
    }

    public Order cancelOrderByAdmin(User admin, String orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new ResourceNotFoundException("Order not found"));

        Restaurant restaurant = restaurantRepository.findById(order.getRestaurantId())
                .orElseThrow(() -> new ResourceNotFoundException("Restaurant not found"));

        System.out.println(order + " " + restaurant);
        System.out.println("RestaurantOwner id :" + restaurant.getOwnerId());
        if (restaurant.getOwnerId() == null ||
                !restaurant.getOwnerId().equals(admin.getId())) {
            throw new ForbiddenException("Not authorized to manage this restaurant");
        }

        if (!OrderStateMachine.canTransition(order.getStatus(), OrderStatus.CANCELLED)) {
            throw new ConflictException("Order cannot be cancelled at this stage");
        }

//        order.setStatus(OrderStatus.CANCELLED);
//        order.setUpdatedAt(LocalDateTime.now());
        order.markStatus(OrderStatus.CANCELLED);

        return orderRepository.save(order);
    }


}
