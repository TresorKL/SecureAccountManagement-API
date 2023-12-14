package com.example.secureaccountapi.repository;

import com.example.secureaccountapi.entity.LoginActivityLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LoginActivityLogRepo extends JpaRepository<LoginActivityLog,Integer> {
}
