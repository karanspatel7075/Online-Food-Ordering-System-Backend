package com.PavitraSoft.FoodApplication.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PaymentDetails {

    private String method; // COD, UPI, CARD
    private String status;  // PENDING, PAID, FAILED
    private String transactionId;

}
