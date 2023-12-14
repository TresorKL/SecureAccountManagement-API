package com.example.secureaccountapi.repository;

import com.example.secureaccountapi.entity.MfaVerificationCode;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;


@Repository
public interface MfaVerificationCodeRepo extends JpaRepository<MfaVerificationCode,Integer> {

    Optional<MfaVerificationCode> findByEmail(String email);
}
