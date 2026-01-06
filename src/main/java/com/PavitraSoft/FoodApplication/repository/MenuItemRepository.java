package com.PavitraSoft.FoodApplication.repository;

import com.PavitraSoft.FoodApplication.model.MenuItem;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface MenuItemRepository extends MongoRepository<MenuItem, String> {
    //
    List<MenuItem> findByRestaurantIdAndIsAvailable(String restaurantId);
    List<MenuItem> findByRestaurantId(String restaurantId);
}
