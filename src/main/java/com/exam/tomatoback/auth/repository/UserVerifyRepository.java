package com.exam.tomatoback.auth.repository;

import com.exam.tomatoback.auth.model.UserVerify;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserVerifyRepository extends JpaRepository<UserVerify, Long> {
  Optional<UserVerify> findByToken(String token);
}
