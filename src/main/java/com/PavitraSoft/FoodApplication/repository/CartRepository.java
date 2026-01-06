package com.PavitraSoft.FoodApplication.repository;

import com.PavitraSoft.FoodApplication.model.Cart;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface CartRepository extends MongoRepository<Cart, String> {
    //
    Cart findByUserId(String userId);
}
