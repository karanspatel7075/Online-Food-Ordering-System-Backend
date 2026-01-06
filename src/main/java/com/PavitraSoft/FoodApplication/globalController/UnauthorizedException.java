package com.PavitraSoft.FoodApplication.globalController;

public class UnauthorizedException extends RuntimeException {
    public UnauthorizedException(String message) {
        super(message);
    }
}
