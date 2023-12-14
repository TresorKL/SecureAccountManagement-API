package com.example.secureaccountapi.repository;

import com.example.secureaccountapi.entity.UserPasswordRecovery;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserPasswordRecoveryRepo extends JpaRepository<UserPasswordRecovery,Integer> {
}
