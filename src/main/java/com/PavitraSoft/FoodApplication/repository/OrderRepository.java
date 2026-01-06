package com.PavitraSoft.FoodApplication.repository;

import com.PavitraSoft.FoodApplication.model.Order;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface OrderRepository extends MongoRepository<Order, String> {
    //
    List<Order> findByUserId(String id);
}
