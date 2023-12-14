package com.example.secureaccountapi.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class SecuritySettingRequest {

    private String email;
    private boolean isMfaEnabled;
    private boolean userSecondaryEmailForMfa;

    private boolean sendCodeForMfa;
    private boolean askConfirmationQuestionForMfa;
}
