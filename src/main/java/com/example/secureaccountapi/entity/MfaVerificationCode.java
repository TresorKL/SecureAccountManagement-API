package com.example.secureaccountapi.entity;


import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.UUID;

@Entity
@Data
@NoArgsConstructor
@Table(name = "mfa_verification_code", schema = "public")
public class MfaVerificationCode {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;

    private String email;
    private int code;
    private UUID verificationToken;
    private Date issuedAt;
    private Date expireAt;

}
