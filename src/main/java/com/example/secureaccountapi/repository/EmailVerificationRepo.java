package com.example.secureaccountapi.repository;

import com.example.secureaccountapi.entity.EmailVerification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EmailVerificationRepo extends JpaRepository<EmailVerification,Integer> {

    EmailVerification findByEmail(String email);
}
