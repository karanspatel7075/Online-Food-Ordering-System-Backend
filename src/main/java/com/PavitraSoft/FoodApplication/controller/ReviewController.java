package com.PavitraSoft.FoodApplication.controller;

import com.PavitraSoft.FoodApplication.dto.ReviewRequestDto;
import com.PavitraSoft.FoodApplication.dto.ReviewResponseDto;
import com.PavitraSoft.FoodApplication.model.Review;
import com.PavitraSoft.FoodApplication.model.User;
import com.PavitraSoft.FoodApplication.service.ReviewService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/reviews")
@RequiredArgsConstructor
public class ReviewController {

    private final ReviewService reviewService;

    @PostMapping("/{orderId}")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<String> addReview(@AuthenticationPrincipal User user, @PathVariable String orderId, @RequestBody @Valid ReviewRequestDto dto) {
        reviewService.addReview(user, orderId, dto);
        return ResponseEntity.ok("Review submitted successfully");
    }

    @GetMapping("/{restaurantId}/reviews")
    public List<ReviewResponseDto> getReviews(@PathVariable String restaurantId) {
        return reviewService.getReviewForRestaurant(restaurantId);
    }
}
