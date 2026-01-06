package com.PavitraSoft.FoodApplication.service;

import com.PavitraSoft.FoodApplication.dto.AddToCartRequest;
import com.PavitraSoft.FoodApplication.dto.CartItemResponseDto;
import com.PavitraSoft.FoodApplication.dto.CartResponseDto;
import com.PavitraSoft.FoodApplication.dto.CreateOrderRequestDto;
import com.PavitraSoft.FoodApplication.enums.OrderStatus;
import com.PavitraSoft.FoodApplication.globalController.BadRequestException;
import com.PavitraSoft.FoodApplication.globalController.ConflictException;
import com.PavitraSoft.FoodApplication.globalController.ResourceNotFoundException;
import com.PavitraSoft.FoodApplication.model.*;
import com.PavitraSoft.FoodApplication.repository.CartRepository;
import com.PavitraSoft.FoodApplication.repository.MenuItemRepository;
import com.PavitraSoft.FoodApplication.repository.OrderRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Optional;

@Service
public class CartService {

    private final MenuItemRepository itemRepository;
    private final CartRepository cartRepository;
    private final OrderRepository orderRepository;

    public CartService(MenuItemRepository itemRepository, CartRepository cartRepository, OrderService orderService, OrderRepository orderRepository) {
        this.itemRepository = itemRepository;
        this.cartRepository = cartRepository;
        this.orderRepository = orderRepository;
    }

    public CartResponseDto addToCart(User user, AddToCartRequest dto) {
        Cart cart = cartRepository.findByUserId(user.getId());

        if (cart == null) {
            cart = Cart.builder()
                    .userId(user.getId())
                    .restaurantId(dto.getRestaurantId())
                    .items(new ArrayList<>())
                    .totalAmount(0.0)
                    .updatedAt(LocalDateTime.now())
                    .build();
        }

        if(!cart.getRestaurantId().equals(dto.getRestaurantId())) {
            throw new ConflictException("Clear cart before ordering from another restaurant");
        }

        MenuItem item = itemRepository.findById(dto.getMenuItemId()).orElseThrow(() -> new ResourceNotFoundException("Item not found"));

        Optional<OrderItem> existingItem = cart.getItems().stream()
                        .filter(i -> i.getMenuItemId().equals(dto.getMenuItemId()))
                                .findFirst();

        if(existingItem.isPresent()) {
            existingItem.get().setQuantity(
                    existingItem.get().getQuantity() + dto.getQuantity()
            );
        } else {
            cart.getItems().add(
                    OrderItem.builder()
                            .menuItemId(item.getId())
                            .name(item.getName())
                            .price(item.getPrice())
                            .quantity(dto.getQuantity())
                            .build()
            );
        }

        cart.setTotalAmount(
                cart.getItems().stream()
                .mapToDouble(i -> i.getPrice() * i.getQuantity())
                        .sum()
        );

        cart.setUpdatedAt(LocalDateTime.now());
        cartRepository.save(cart);
        return mapToCartResponse(cart);
    }

    public CartResponseDto  viewCart(User user) {
        Cart cart = cartRepository.findByUserId(user.getId());

        if(cart == null) {
            throw new ResourceNotFoundException("Cart not found");
        }

        return mapToCartResponse(cart);
    }

    public Order createOrderFromCart(User user, Cart cart) {
        return Order.builder()
                .userId(user.getId())
                .restaurantId(cart.getRestaurantId())
                .items(cart.getItems())
                .totalAmount(cart.getTotalAmount())
                .status(OrderStatus.PLACED)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();
    }

    private CartResponseDto mapToCartResponse(Cart cart) {
        return CartResponseDto.builder()
                .cartId(cart.getId())
                .restaurantId(cart.getRestaurantId())
                .items(
                        cart.getItems().stream()
                                .map(item -> CartItemResponseDto.builder()
                                        .menuItemId(item.getMenuItemId())
                                        .name(item.getName())
                                        .price(item.getPrice())
                                        .quantity(item.getQuantity())
                                        .totalPrice(item.getPrice() * item.getQuantity())
                                        .build())
                                .toList()
                )
                .totalAmount(cart.getTotalAmount())
                .updatedAt(cart.getUpdatedAt())
                .build();
    }


    @Transactional
    public Order checkout(User user, CreateOrderRequestDto dto) {

        Cart cart = cartRepository.findByUserId(user.getId());

        if (cart == null || cart.getItems().isEmpty()) {
            throw new BadRequestException("Cart is empty");
        }

        Order order = createOrderFromCart(user, cart);
        orderRepository.save(order);
        cartRepository.delete(cart);

        return order;
    }


}
