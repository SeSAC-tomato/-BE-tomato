package com.exam.tomatoback.user.service;

import com.exam.tomatoback.infrastructure.exception.TomatoException;
import com.exam.tomatoback.infrastructure.exception.TomatoExceptionCode;
import com.exam.tomatoback.user.model.User;
import com.exam.tomatoback.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
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

  @Override
  public User getCurrentUser() {
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    if (authentication == null || !authentication.isAuthenticated()) {
      throw new TomatoException(TomatoExceptionCode.UNABLE_AUTH_INFO);
    }
    Object principal = authentication.getPrincipal();
    if (principal instanceof UserDetails userDetails) {
      String username = userDetails.getUsername();
      return getOptionalUser(username).orElseThrow(
          () -> new TomatoException(TomatoExceptionCode.USER_NOT_FOUND)
      );
    } else {
      throw new TomatoException(TomatoExceptionCode.UNABLE_AUTH_INFO);
    }
  }

  @Override
  public UserDetails getCurrentUserDetails() {
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    if (authentication == null || !authentication.isAuthenticated()) {
      throw new TomatoException(TomatoExceptionCode.UNABLE_AUTH_INFO);
    }
    Object principal = authentication.getPrincipal();
    if (principal instanceof UserDetails userDetails) {
      return userDetails;
    } else {
      throw new TomatoException(TomatoExceptionCode.UNABLE_AUTH_INFO);
    }
  }
}
