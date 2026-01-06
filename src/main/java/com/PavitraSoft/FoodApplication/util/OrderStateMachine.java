package com.PavitraSoft.FoodApplication.util;

import com.PavitraSoft.FoodApplication.enums.OrderStatus;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.Set;

@Component
public class OrderStateMachine {
    private static final Map<OrderStatus, Set<OrderStatus>> ALLOWED_TRANSITIONS = Map.of(
            OrderStatus.CREATED, Set.of(OrderStatus.PLACED, OrderStatus.CANCELLED),
            OrderStatus.PLACED, Set.of(OrderStatus.ACCEPTED, OrderStatus.CANCELLED),
            OrderStatus.ACCEPTED, Set.of(OrderStatus.PREPARING, OrderStatus.CANCELLED),
            OrderStatus.PREPARING, Set.of(OrderStatus.OUT_FOR_DELIVERY, OrderStatus.CANCELLED),
            OrderStatus.OUT_FOR_DELIVERY, Set.of(OrderStatus.DELIVERED)
    );

    public static boolean canTransition(OrderStatus current, OrderStatus next) {
        return ALLOWED_TRANSITIONS
                .getOrDefault(current, Set.of())
                .contains(next);
    }


}
