package com.example.secureaccountapi.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class UpdateContactRequest {

    private String email;
    private String secondEmail;
    private String phoneNumber;
}
