package com.example.secureaccountapi.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
public class EmailVerificationRequest {
    private String email;
    private int code;
}
