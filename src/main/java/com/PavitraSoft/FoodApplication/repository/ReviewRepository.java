package com.PavitraSoft.FoodApplication.repository;

import com.PavitraSoft.FoodApplication.model.Review;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface ReviewRepository extends MongoRepository<Review, String> {
    //
    boolean existsByOrderId(String orderId);
    List<Review> findByRestaurantId(String restaurantId);
}
