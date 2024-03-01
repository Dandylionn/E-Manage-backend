package com.udemy.spring.bootrestfulwebservices.repository;

import com.udemy.spring.bootrestfulwebservices.entity.User;
import jakarta.persistence.Id;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
}
