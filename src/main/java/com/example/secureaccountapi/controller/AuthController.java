package com.example.secureaccountapi.controller;

import com.example.secureaccountapi.dto.*;
import com.example.secureaccountapi.entity.User;
import com.example.secureaccountapi.repository.UserRepo;
import com.example.secureaccountapi.service.AuthService;
import com.example.secureaccountapi.service.EmailVerificationService;
import com.example.secureaccountapi.service.impl.EmailServiceImpl;
import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping(value = "/api/v1/auth",consumes = MediaType.APPLICATION_JSON_VALUE)
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @Autowired
    private UserRepo userRepo;

    @Autowired
    private EmailVerificationService emailVerificationService;

    @PostMapping("/signup")
    public ResponseEntity<User> signup(@RequestBody SignUpRequest signUpRequest) throws MessagingException {
        return ResponseEntity.ok(authService.signUp(signUpRequest));
    }

    @PostMapping("/signin")
    public ResponseEntity<JwtAuthResponse> signIn(@RequestBody SignInRequest signInRequest)throws MessagingException {
        return ResponseEntity.ok(authService.signIn(signInRequest));
    }

    @PostMapping("/signin/mfa")
    public ResponseEntity<JwtAuthResponse>mfaVerification(@RequestBody MfaVerificationRequest mfaVerificationRequest){
        return ResponseEntity.ok(authService.verifyMfaCode(mfaVerificationRequest));
    }

    @PostMapping(value="/refresh",consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<JwtAuthResponse> refresh(@RequestBody RefreshTokenRequest refreshTokenRequest){
        return ResponseEntity.ok(authService.refreshToken(refreshTokenRequest));
    }

    @PostMapping(value="/email/verify",consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> verifyEmail(@RequestBody EmailVerificationRequest emailVerificationRequest){

        String emailVerificationResponseStr= emailVerificationService.verifyCode(emailVerificationRequest);

        String[] responseStr=emailVerificationResponseStr.split("#");

        int responseCode;
        String message=responseStr[1];

        Map<String, Object> map = new HashMap<String, Object>();

        if (responseStr[0].equals("0")){
            responseCode=403;
        } else {
            responseCode=200;
        }

        map.put("status",HttpStatusCode.valueOf(responseCode).value());
        map.put("message",message);

        return new ResponseEntity<Object>(map, HttpStatusCode.valueOf(responseCode));
    }

    @PostMapping("/email/resend")
    public ResponseEntity<Object> resendEmailVerificationCode(@RequestBody EmailVerificationRequest emailVerificationRequest) throws MessagingException {

        Optional<User> user = userRepo.findByEmail(emailVerificationRequest.getEmail());
        Map<String, Object> map = new HashMap<String, Object>();

        int responseCode;
        String message;

        if (user.isPresent()){
            responseCode=200;
            message="New Email Verification Successfully sent";
            emailVerificationService.sendVerificationCode(emailVerificationRequest.getEmail(),1);
        }else {
            responseCode=403;
            message="Invalid email address";
        }

        map.put("status",HttpStatusCode.valueOf(responseCode).value());
        map.put("message",message);

        return new ResponseEntity<Object>(map, HttpStatusCode.valueOf(responseCode));
    }

    @PostMapping(value="/update/password/request")
    public ResponseEntity<Object> requestPasswordReset(@RequestBody EmailVerificationRequest emailVerificationRequest) throws MessagingException {

        Optional<User> user = userRepo.findByEmail(emailVerificationRequest.getEmail());
        Map<String, Object> map = new HashMap<String, Object>();

        int responseCode;
        String message;

        if (user.isPresent()){
            responseCode=200;
            message="New Email Verification Successfully sent";
            emailVerificationService.sendVerificationCode(emailVerificationRequest.getEmail(),2);
        }else {
            responseCode=403;
            message="Invalid email address";
        }

        map.put("status",HttpStatusCode.valueOf(responseCode).value());
        map.put("message",message);

        return new ResponseEntity<Object>(map, HttpStatusCode.valueOf(responseCode));
    }


    @PostMapping("/update/password/reset")
    public ResponseEntity<Object> resetPassword(@RequestBody ResetPasswordRequest resetPasswordRequest){

        return authService.resetPassword(resetPasswordRequest);
    }






}
