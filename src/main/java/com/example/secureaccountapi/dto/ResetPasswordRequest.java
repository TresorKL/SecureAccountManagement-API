package com.example.secureaccountapi.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
public class ResetPasswordRequest {

    private int code;
    private String newPassword;
    private String email;
}
