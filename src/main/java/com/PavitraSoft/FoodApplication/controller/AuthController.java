package com.PavitraSoft.FoodApplication.controller;

import com.PavitraSoft.FoodApplication.auth.*;
import com.PavitraSoft.FoodApplication.dto.ForgotPasswordRequestDto;
import com.PavitraSoft.FoodApplication.dto.ResetPasswordRequestDto;
import jakarta.mail.MessagingException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.io.UnsupportedEncodingException;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/register")
    public ResponseEntity<RegisterResponseDto> register(@Valid @RequestBody RegisterRequestDto requestDto) throws IOException {
        RegisterResponseDto responseDto = authService.register(requestDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(responseDto);
    }

    @PostMapping("/register-restaurant-admin")
    public ResponseEntity<RegisterResponseDto> registerAdmin(@Valid @RequestBody RegisterRequestDto requestDto) throws IOException {
        RegisterResponseDto responseDto = authService.register(requestDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(responseDto);
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponseDto> login(@Valid @RequestBody LoginRequestDto requestDto) throws IOException {
        LoginResponseDto loginResponseDto = authService.login(requestDto);
        return ResponseEntity.ok(loginResponseDto);
    }

    @PostMapping("/forgot-password")
    public ResponseEntity<String> forgetPassword(@Valid @RequestBody ForgotPasswordRequestDto dto) {
        authService.sendOtp(dto);
        return ResponseEntity.ok("OTP sent to email");
    }

    @PostMapping("/reset-password")
    public ResponseEntity<String> resetPassword(@Valid @RequestBody ResetPasswordRequestDto dto) throws MessagingException, UnsupportedEncodingException {
        System.out.println(dto.getNewPassword() + " " + dto.getEmail() + " " + dto.getOtp());
        authService.resetPassword(dto);
        return ResponseEntity.ok("Password reset successful");
    }
}
