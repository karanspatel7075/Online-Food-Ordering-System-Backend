package com.PavitraSoft.FoodApplication.globalController;

public class ForbiddenException extends RuntimeException {
    public ForbiddenException(String message) {
        super(message);
    }
}
