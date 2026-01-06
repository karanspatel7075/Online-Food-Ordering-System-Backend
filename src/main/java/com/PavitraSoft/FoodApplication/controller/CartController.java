package com.PavitraSoft.FoodApplication.controller;

import com.PavitraSoft.FoodApplication.dto.AddToCartRequest;
import com.PavitraSoft.FoodApplication.dto.CartResponseDto;
import com.PavitraSoft.FoodApplication.dto.CreateOrderRequestDto;
import com.PavitraSoft.FoodApplication.model.Cart;
import com.PavitraSoft.FoodApplication.model.Order;
import com.PavitraSoft.FoodApplication.model.User;
import com.PavitraSoft.FoodApplication.service.CartService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/cart")
@RequiredArgsConstructor
public class CartController {

    private final CartService cartService;

    @PostMapping("/add")
    public ResponseEntity<CartResponseDto> addItemToCart(@AuthenticationPrincipal User user, @Valid @RequestBody AddToCartRequest dto) {
        return ResponseEntity.ok(cartService.addToCart(user, dto));
    }

    @GetMapping
    public ResponseEntity<CartResponseDto > viewCart(@AuthenticationPrincipal User user) {
        return ResponseEntity.ok(cartService.viewCart(user));
    }

    @PostMapping("/checkout")
    public ResponseEntity<Order> checkout(@AuthenticationPrincipal User user, @Valid @RequestBody CreateOrderRequestDto dto) {
        return ResponseEntity.ok(cartService.checkout(user, dto));
    }
}
