package com.example.secureaccountapi.service;

import jakarta.mail.MessagingException;

public interface EmailService {

    void sendHtmlMail(String to, String subject, String content, boolean isContentHTML) throws MessagingException;
}
