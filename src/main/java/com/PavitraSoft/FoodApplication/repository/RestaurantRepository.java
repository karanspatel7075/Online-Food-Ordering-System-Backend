package com.PavitraSoft.FoodApplication.repository;

import com.PavitraSoft.FoodApplication.dto.RestaurantResponseDto;
import com.PavitraSoft.FoodApplication.enums.RestaurantStatus;
import com.PavitraSoft.FoodApplication.model.Restaurant;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface RestaurantRepository extends MongoRepository<Restaurant, String> {
    //
    List<Restaurant> findByActiveTrue();
    Page<Restaurant> findByActiveTrueAndNameContainingIgnoreCaseOrActiveTrueAndCategoriesContainingIgnoreCase(String name, String category, Pageable pageable);
    Page<Restaurant> findByActiveTrue(Pageable pageable);

    List<Restaurant> findByStatus(RestaurantStatus restaurantStatus);
}
