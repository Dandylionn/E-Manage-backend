package com.udemy.spring.bootrestfulwebservices.repository;

import com.udemy.spring.bootrestfulwebservices.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);
}
