package com.example.secureaccountapi.service;

import com.example.secureaccountapi.dto.*;
import com.example.secureaccountapi.entity.User;
import jakarta.mail.MessagingException;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;


public interface AuthService {

    User signUp(SignUpRequest signUpRequest) throws MessagingException;
    JwtAuthResponse signIn(SignInRequest signInRequest) throws MessagingException;
    JwtAuthResponse verifyMfaCode(MfaVerificationRequest mfaVerificationRequest);
    ResponseEntity<Object> requestPasswordRecovery(@RequestBody ResetPasswordRequest resetPasswordRequest) throws MessagingException;
    ResponseEntity<Object> resetPassword(ResetPasswordRequest resetPasswordRequest);
    JwtAuthResponse refreshToken(RefreshTokenRequest refreshTokenRequest);
}
