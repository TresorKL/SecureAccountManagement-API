package com.example.secureaccountapi.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor
@Data
@Table(name = "security_setting", schema = "public")
public class SecuritySetting {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;

     private boolean isMfaEnabled;
     private boolean userSecondaryEmailForMfa;

     private boolean sendCodeForMfa;
     private boolean askConfirmationQuestionForMfa;



}
