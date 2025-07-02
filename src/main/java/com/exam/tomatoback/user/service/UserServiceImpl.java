package com.exam.tomatoback.user.service;

import com.exam.tomatoback.user.model.User;
import com.exam.tomatoback.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
  private final UserRepository repository;

  @Override
  public User save(User user) {
    return repository.save(user);
  }

  @Override
  public boolean existsByEmail(String email) {
    return repository.existsByEmail(email);
  }

  @Override
  public boolean existsByNickname(String nickname) {
    return repository.existsByNickname(nickname);
  }

  @Override
  public Optional<User> getOptionalUser(String email) {
    return repository.findByEmail(email);
  }
}
