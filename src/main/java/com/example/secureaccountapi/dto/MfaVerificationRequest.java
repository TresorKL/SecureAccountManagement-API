package com.example.secureaccountapi.dto;

import lombok.Data;

import java.util.Date;
import java.util.UUID;

@Data
public class MfaVerificationRequest {

    private String email;
    private int code;
    private UUID verificationToken;

}
