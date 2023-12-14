package com.example.secureaccountapi.service;


import com.example.secureaccountapi.dto.EmailVerificationRequest;
import jakarta.mail.MessagingException;

public interface EmailVerificationService {

     void sendVerificationCode(String email,int reason) throws MessagingException;

     void resendVerificationCode(String email)  throws MessagingException;

     String verifyCode(EmailVerificationRequest emailVerificationRequest);
}
