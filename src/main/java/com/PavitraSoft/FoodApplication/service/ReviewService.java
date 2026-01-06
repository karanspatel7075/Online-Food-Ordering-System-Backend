package com.PavitraSoft.FoodApplication.service;

import com.PavitraSoft.FoodApplication.dto.ReviewRequestDto;
import com.PavitraSoft.FoodApplication.dto.ReviewResponseDto;
import com.PavitraSoft.FoodApplication.enums.OrderStatus;
import com.PavitraSoft.FoodApplication.globalController.BadRequestException;
import com.PavitraSoft.FoodApplication.globalController.ConflictException;
import com.PavitraSoft.FoodApplication.globalController.ForbiddenException;
import com.PavitraSoft.FoodApplication.globalController.ResourceNotFoundException;
import com.PavitraSoft.FoodApplication.model.Order;
import com.PavitraSoft.FoodApplication.model.Restaurant;
import com.PavitraSoft.FoodApplication.model.Review;
import com.PavitraSoft.FoodApplication.model.User;
import com.PavitraSoft.FoodApplication.repository.OrderRepository;
import com.PavitraSoft.FoodApplication.repository.RestaurantRepository;
import com.PavitraSoft.FoodApplication.repository.ReviewRepository;
import com.PavitraSoft.FoodApplication.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ReviewService {

    private final ReviewRepository reviewRepository;
    private final OrderRepository orderRepository;
    private final RestaurantRepository restaurantRepository;
    private final UserRepository userRepository;

    public void addReview(User user, String orderId, ReviewRequestDto dto) {
        Order order = orderRepository.findById(orderId).orElseThrow(() -> new ResourceNotFoundException("Order not found"));

        // only order owner
        if (!order.getUserId().equals(user.getId())) {
            throw new ForbiddenException("Not your order");
        }

        // only after delivery
        if (order.getStatus() != OrderStatus.DELIVERED) {
            throw new BadRequestException("Order not delivered yet");
        }

        // only one review per order
        if (reviewRepository.existsByOrderId(orderId)) {
            throw new ConflictException("Review already submitted");
        }

        Review review = Review.builder()
                .restaurantId(order.getRestaurantId())
                .userId(order.getUserId())
                .orderId(order.getId())
                .rating(dto.getRating())
                .comment(dto.getComment())
                .createAt(LocalDateTime.now())
                .build();

        reviewRepository.save(review);

        updateRestaurantRating(order.getRestaurantId(), dto.getRating());
    }

    // avg rating calculation
    private void updateRestaurantRating(String restaurantId, int newRating) {
        Restaurant restaurant = restaurantRepository.findById(restaurantId).orElseThrow(() -> new ResourceNotFoundException("Restaurant not found"));

        long count = restaurant.getRatingCount();
        double avg = restaurant.getRating();

        double updatedAvg = ((avg * count) + newRating) / (count + 1);

        restaurant.setRating(updatedAvg);
        restaurant.setRatingCount(count + 1);
        restaurant.setUpdatedAt(LocalDateTime.now());

        restaurantRepository.save(restaurant);
    }


    public List<ReviewResponseDto> getReviewForRestaurant(String restaurantId) {

        return reviewRepository.findByRestaurantId(restaurantId)
                .stream()
                .map(r -> {
                    User user = userRepository.findById(r.getUserId()).orElse(null);
                    return ReviewResponseDto.builder()
                            .userName(user != null ? user.getName() : "User")
                            .rating(r.getRating())
                            .comment(r.getComment())
                            .createdAt(r.getCreateAt())
                            .build();
                })
                .toList();
    }
}
