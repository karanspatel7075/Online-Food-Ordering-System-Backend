package com.PavitraSoft.FoodApplication.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Document(collection = "reviews")
public class Review {

    @Id
    private String id;

    private String restaurantId;
    private String userId;
    private String orderId;

    private int rating;
    private String comment;

    private LocalDateTime createAt;


}
