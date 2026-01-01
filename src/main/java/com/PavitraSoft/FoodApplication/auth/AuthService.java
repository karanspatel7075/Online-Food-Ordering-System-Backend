package com.PavitraSoft.FoodApplication.auth;

import com.PavitraSoft.FoodApplication.dto.ForgotPasswordRequestDto;
import com.PavitraSoft.FoodApplication.dto.ResetPasswordRequestDto;
import com.PavitraSoft.FoodApplication.enums.Role;
import com.PavitraSoft.FoodApplication.model.PasswordResetOtp;
import com.PavitraSoft.FoodApplication.model.User;
import com.PavitraSoft.FoodApplication.repository.PasswordResetOtpRepository;
import com.PavitraSoft.FoodApplication.repository.UserRepository;
import com.PavitraSoft.FoodApplication.service.EmailServiceImple;
import com.PavitraSoft.FoodApplication.util.OtpGenerator;
import jakarta.mail.MessagingException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.time.LocalDateTime;

@Service
public class AuthService {

    private final AuthenticationManager authenticationManager;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthTokenGen authTokenGen;

    private final OtpGenerator otpGenerator;
    private final PasswordResetOtpRepository resetOtpRepository;
    private final EmailServiceImple emailServiceImple;

    public AuthService(AuthenticationManager authenticationManager, UserRepository userRepository, PasswordEncoder passwordEncoder, AuthTokenGen authTokenGen, OtpGenerator otpGenerator, PasswordResetOtpRepository resetOtpRepository, EmailServiceImple emailServiceImple) {
        this.authenticationManager = authenticationManager;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.authTokenGen = authTokenGen;
        this.otpGenerator = otpGenerator;
        this.resetOtpRepository = resetOtpRepository;
        this.emailServiceImple = emailServiceImple;
    }

        public LoginResponseDto login(LoginRequestDto loginRequestDto) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequestDto.getEmail(), loginRequestDto.getPassword())

        );

        User user = (User) authentication.getPrincipal();

        String token = authTokenGen.generateAccessToken(user);

        return new LoginResponseDto(token, user.getId());
    }

    public RegisterResponseDto register(RegisterRequestDto registerRequestDto) throws IOException {
        if(userRepository.findByEmail(registerRequestDto.getEmail()).isPresent()) {
            throw new IllegalArgumentException("User already exists");
        }

        User user = User.builder()
                .name(registerRequestDto.getName())
                .email(registerRequestDto.getEmail())
                .password(passwordEncoder.encode(registerRequestDto.getPassword()))
                .phone(registerRequestDto.getPhone())
                .role(Role.USER)
                .active(true)
                .createdAt(LocalDateTime.now())
                .build();

        userRepository.save(user);
        return RegisterResponseDto.builder()
                .name(user.getName())
                .email(user.getEmail())
                .build();
    }

    public void sendOtp(ForgotPasswordRequestDto dto) {

        User user = userRepository.findByEmail(dto.getEmail()).orElseThrow(() -> new RuntimeException("No user found"));

        String otp = otpGenerator.generateOtp();

        PasswordResetOtp resetOtp = PasswordResetOtp.builder()
                .email(user.getEmail())
                .otp(otp)
                .expiryTime(LocalDateTime.now().plusMinutes(5))
                .build();

        System.out.println("AuthService sendOtp() CALLED");
        System.out.println("Email: " + user.getEmail());
        System.out.println("OTP: " + otp);

        resetOtpRepository.save(resetOtp);
        emailServiceImple.sendOtp(user.getEmail(), otp);
    }

    public void resetPassword(ResetPasswordRequestDto dto) throws MessagingException, UnsupportedEncodingException {

        PasswordResetOtp savedOtp = resetOtpRepository.findByEmail(dto.getEmail()).orElseThrow(() -> new RuntimeException("OTP not found"));

        if(savedOtp.getExpiryTime().isBefore(LocalDateTime.now())) {
            throw  new RuntimeException("OTP expired");
        }

        if(!savedOtp.getOtp().equals(dto.getOtp())) {
            throw new RuntimeException("Invalid OTP");
        }

        User user = userRepository.findByEmail(dto.getEmail()).orElseThrow(() -> new RuntimeException("Email invalid"));
        user.setPassword(passwordEncoder.encode(dto.getNewPassword()));
        userRepository.save(user);

        // Delete the otp for security purpose
        resetOtpRepository.delete(savedOtp);

        try {
            emailServiceImple.sendConfirmationMail(user);
        } catch (Exception e) {
            // optional: log error (don’t fail reset if mail fails)
            System.out.println("Failed to send confirmation email: " + e.getMessage());
        }
    }

}
