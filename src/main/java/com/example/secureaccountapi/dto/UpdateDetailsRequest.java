package com.example.secureaccountapi.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class UpdateDetailsRequest {

    private String email;
    private String firstName;
    private String lastName;
    private String dataOfBirth;
    private String physicalAddress;
}
