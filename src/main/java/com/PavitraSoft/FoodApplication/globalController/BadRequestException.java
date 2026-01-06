package com.PavitraSoft.FoodApplication.globalController;

public class BadRequestException extends RuntimeException {
    public BadRequestException(String message) {
        super(message);
    }
}
