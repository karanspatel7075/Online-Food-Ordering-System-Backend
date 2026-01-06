package com.PavitraSoft.FoodApplication.controller;

import com.PavitraSoft.FoodApplication.dto.ItemRequestDto;
import com.PavitraSoft.FoodApplication.dto.RestaurantRequestDto;
import com.PavitraSoft.FoodApplication.dto.RestaurantResponseDto;
import com.PavitraSoft.FoodApplication.model.MenuItem;
import com.PavitraSoft.FoodApplication.model.User;
import com.PavitraSoft.FoodApplication.service.MenuService;
import com.PavitraSoft.FoodApplication.service.RestaurantService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.security.PublicKey;
import java.util.List;

@RestController
@RequestMapping("/api/restaurants")
@RequiredArgsConstructor
@Tag(name = "Restaurants")
public class RestaurantController {

    private final RestaurantService restaurantService;
    private final MenuService menuService;

    @PostMapping
    @PreAuthorize("hasRole('RESTAURANT_ADMIN')")
    public ResponseEntity<RestaurantResponseDto> create(@AuthenticationPrincipal User user, @Valid @RequestBody RestaurantRequestDto requestDto) {
        return ResponseEntity.ok(restaurantService.create(requestDto, user));
    }

    @GetMapping
    public List<RestaurantResponseDto> getAllRestaurants(@RequestParam(required = false) String search, @RequestParam(defaultValue = "0") int page,
                                                         @RequestParam(defaultValue = "10") int size) {
        return restaurantService.getAll(search, page, size);
    }

    @GetMapping("/{restaurantId}")
    public ResponseEntity<RestaurantResponseDto> getRestaurant(@PathVariable String restaurantId) {
        return ResponseEntity.ok(restaurantService.getById(restaurantId));
    }

    @GetMapping("/{restaurantId}/menu")
    public ResponseEntity< List<MenuItem> > getMenu(@PathVariable String restaurantId) {
        System.out.println("CONTROLLER restaurantId = " + restaurantId);
        return ResponseEntity.ok(menuService.getMenuForRestaurant(restaurantId));
    }

    @PostMapping("/{restaurantId}/menu")
    @PreAuthorize("hasRole('RESTAURANT_ADMIN')")
    public ResponseEntity<String> addItem(@AuthenticationPrincipal User admin, @PathVariable String restaurantId, @Valid @RequestBody ItemRequestDto dto) {
        System.out.println("CONTROLLER restaurantId = " + restaurantId);
        dto.setRestaurantId(restaurantId);

        menuService.addItem(admin, dto);
        System.out.println("DTO restaurantId = " + dto.getRestaurantId());
        return ResponseEntity.ok("Item added to menu successfully");
    }

    @PutMapping("/{restaurantId}/menu/{itemId}")
    @PreAuthorize("hasRole('RESTAURANT_ADMIN')")
    public ResponseEntity<MenuItem> updateItem(@AuthenticationPrincipal User admin, @RequestBody ItemRequestDto dto,
                                               @PathVariable String restaurantId, @PathVariable String itemId) {
        return ResponseEntity.ok(menuService.updateItem(admin, dto, restaurantId, itemId));
    }

    @DeleteMapping("/{restaurantId}/menu/{itemId}")
    @PreAuthorize("hasRole('RESTAURANT_ADMIN')")
    public String deleteItem(@AuthenticationPrincipal User admin ,
                                               @PathVariable String restaurantId, @PathVariable String itemId) {
        menuService.deleteItem(admin, restaurantId, itemId);
        return "Item successfully deleted";
    }

//    /api/restaurants/695769b96c432f61506f5a85/menu/6958168c33212c17f0853f87

}
