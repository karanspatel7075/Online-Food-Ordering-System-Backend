package com.PavitraSoft.FoodApplication.interfaces;

import com.PavitraSoft.FoodApplication.dto.ResetPasswordRequestDto;
import com.PavitraSoft.FoodApplication.model.User;
import jakarta.mail.MessagingException;

import java.io.UnsupportedEncodingException;

public interface EmailService {
    //
    public void sendMail(String to, String subject, String body);

    public void sendConfirmationMail(User user) throws MessagingException, UnsupportedEncodingException;

    public void sendSMS(String phoneNumber, String message);
}
