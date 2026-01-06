package com.PavitraSoft.FoodApplication.dto;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class ReviewResponseDto {
    private String userName;
    private int rating;
    private String comment;
    private LocalDateTime createdAt;
}
