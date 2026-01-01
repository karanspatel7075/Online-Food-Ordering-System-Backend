package com.PavitraSoft.FoodApplication.repository;

import com.PavitraSoft.FoodApplication.model.PasswordResetOtp;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface PasswordResetOtpRepository extends MongoRepository<PasswordResetOtp, String> {
    //
    Optional<PasswordResetOtp> findByEmail(String email);
}
