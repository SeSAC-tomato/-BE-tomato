package com.exam.tomatoback.auth.service;

import com.exam.tomatoback.auth.model.RefreshToken;
import com.exam.tomatoback.user.model.User;
import org.springframework.security.core.userdetails.UserDetails;

public interface RefreshTokenService {
    RefreshToken createRefreshToken(UserDetails userDetails);

    Boolean isInvalid(String refreshToken);

    void deleteToken(User currentUser);
}
