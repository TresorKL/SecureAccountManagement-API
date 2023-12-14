package com.example.secureaccountapi.repository;

import com.example.secureaccountapi.entity.SecuritySetting;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SecuritySettingRepo extends JpaRepository<SecuritySetting,Integer> {
}
