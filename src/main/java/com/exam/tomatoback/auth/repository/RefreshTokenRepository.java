package com.exam.tomatoback.auth.repository;

import com.exam.tomatoback.auth.model.RefreshToken;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {
}
