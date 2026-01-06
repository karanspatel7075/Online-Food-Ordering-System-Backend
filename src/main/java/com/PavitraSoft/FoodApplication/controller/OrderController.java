package com.PavitraSoft.FoodApplication.controller;

import com.PavitraSoft.FoodApplication.dto.CreateOrderRequestDto;
import com.PavitraSoft.FoodApplication.dto.OrderResponseDto;
import com.PavitraSoft.FoodApplication.dto.UpdateOrderStatusDto;
import com.PavitraSoft.FoodApplication.enums.OrderStatus;
import com.PavitraSoft.FoodApplication.model.Order;
import com.PavitraSoft.FoodApplication.model.User;
import com.PavitraSoft.FoodApplication.service.OrderService;
import jakarta.servlet.http.PushBuilder;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/orders")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    @PostMapping
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<OrderResponseDto> createOrder(@AuthenticationPrincipal User user, @RequestBody CreateOrderRequestDto dto) {
        return ResponseEntity.ok(orderService.createOrder(user, dto));
    }

    @GetMapping("/my")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<List<OrderResponseDto>> getMyOrders(@AuthenticationPrincipal User user) {
        return ResponseEntity.ok(orderService.getMyOrder(user));
    }

    @PutMapping("/{orderId}/status")
    @PreAuthorize("hasRole('RESTAURANT_ADMIN')")
    public ResponseEntity<Order> updateStatus(@AuthenticationPrincipal User admin, @PathVariable String orderId, @RequestBody UpdateOrderStatusDto dto) {
        System.out.println("AUTH USER = " + admin);
        return ResponseEntity.ok(orderService.updateStatus(admin, orderId, dto.getStatus()));
    }

    @PutMapping("/{orderId}/cancel")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<Order> cancelOrder(@AuthenticationPrincipal User user, @PathVariable String orderId) {
        System.out.println("USER = " + user);
        return ResponseEntity.ok(orderService.cancelOrderByUser(user, orderId));
    }

    @PutMapping("/{orderId}/admin/cancel")
    @PreAuthorize("hasRole('RESTAURANT_ADMIN')")
    public ResponseEntity<Order> cancelOrderAdmin(@AuthenticationPrincipal User admin, @PathVariable String orderId) {
        System.out.println("AUTH USER = " + admin);
        return ResponseEntity.ok(orderService.cancelOrderByAdmin(admin, orderId));
    }
}
