package com.exam.tomatoback.auth.repository;

import com.exam.tomatoback.auth.model.UserVerify;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserVerifyRepository extends JpaRepository<UserVerify, Long> {
}
