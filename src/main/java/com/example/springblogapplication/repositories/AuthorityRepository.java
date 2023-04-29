package com.example.springblogapplication.repositories;

import com.example.springblogapplication.models.Authority;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AuthorityRepository extends JpaRepository<Authority, String> {}
