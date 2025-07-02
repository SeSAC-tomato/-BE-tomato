package com.exam.tomatoback.auth.service;

import com.exam.tomatoback.auth.model.RefreshToken;
import org.springframework.security.core.userdetails.UserDetails;

public interface RefreshTokenService {
    RefreshToken createRefreshToken(UserDetails userDetails);

    Boolean isInvalid(String refreshToken);
}
