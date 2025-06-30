package com.exam.tomatoback.user.service;

import com.exam.tomatoback.user.model.User;

public interface UserService {
    User save(User user);

    boolean existsByEmail(String email);

    boolean existsByNickname(String nickname);
}
