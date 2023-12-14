package com.example.secureaccountapi.repository;

import com.example.secureaccountapi.entity.Role;
import com.example.secureaccountapi.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepo extends JpaRepository<User,Integer> {

    Optional<User> findByEmail(String email);
    User findByRole(Role role);
}
