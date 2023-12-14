package com.example.secureaccountapi.repository;

import com.example.secureaccountapi.entity.Details;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DetailsRepo extends JpaRepository<Details,Integer> {
}
