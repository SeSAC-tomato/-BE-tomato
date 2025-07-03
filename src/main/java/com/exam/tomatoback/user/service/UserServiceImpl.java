package com.exam.tomatoback.user.service;

import com.exam.tomatoback.infrastructure.exception.TomatoException;
import com.exam.tomatoback.infrastructure.exception.TomatoExceptionCode;
import com.exam.tomatoback.infrastructure.exception.TomatoException;
import com.exam.tomatoback.infrastructure.exception.TomatoExceptionCode;
import com.exam.tomatoback.user.model.Address;
import com.exam.tomatoback.user.model.User;
import com.exam.tomatoback.user.repository.UserRepository;
import com.exam.tomatoback.web.dto.mypage.request.PasswordUpdateRequest;
import com.exam.tomatoback.web.dto.mypage.request.UserUpdateRequest;
import com.exam.tomatoback.web.dto.mypage.response.PasswordUpdatedResponse;
import com.exam.tomatoback.web.dto.mypage.response.UserResponse;
import com.exam.tomatoback.web.dto.mypage.response.UserUpdatedResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static com.exam.tomatoback.infrastructure.exception.TomatoExceptionCode.USER_NOT_FOUND_IN_MYPAGE;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
  private final UserRepository repository;
  private final PasswordEncoder passwordEncoder;

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


    // 마이페이지. userId로 유저 정보 조회해서 응답
  @Override
  public UserResponse getUserById(Long userId) {
    User user = repository.findById(userId).orElseThrow(()-> new TomatoException(USER_NOT_FOUND_IN_MYPAGE));

    String address = null;
    if(user.getAddress() != null) {
      address = user.getAddress().getAddress();
    }
    return new UserResponse(
            user.getNickname(),
            user.getEmail(),
            address
    );
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

  // 마이페이지. userId로 유저 정보 변경
  @Transactional
  @Override
  public UserUpdatedResponse updateUserById(Long userId, UserUpdateRequest request) {

    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    UserDetails userDetails = (UserDetails) authentication.getPrincipal();

//    CertifiedUserId = userDetails.get

    User user = repository.findById(userId).orElseThrow(()-> new TomatoException(TomatoExceptionCode.USER_NOT_FOUND_IN_MYPAGE));

    if(existsByNickname(request.getNickname())) {
      throw new TomatoException(TomatoExceptionCode.DUPLICATE_NICKNAME_IN_MYPAGE);
    }
    user.setNickname(request.getNickname());

    Address address = user.getAddress();
    if(address != null) {
      address.setAddress(request.getAddress());
    } else{
      Address newAddress = Address.builder()
              .user(user)
              .address(request.getAddress())
              .build();
      user.setAddress(newAddress);
    }

    return new UserUpdatedResponse(user.getNickname(), user.getAddress() != null ? user.getAddress().getAddress() : null);
  }

  // 마이페이지. userId로 비밀번호 변경
  @Transactional
  @Override
  public PasswordUpdatedResponse updatePasswordById(Long userId, PasswordUpdateRequest request) {
    User user = repository.findById(userId).orElseThrow(()-> new TomatoException(TomatoExceptionCode.USER_NOT_FOUND_IN_MYPAGE));

    // 1. 현재 비밀번호가 맞는지 확인
    if (!passwordEncoder.matches(request.getCurrentPassword(), user.getPassword())) {
      throw new TomatoException(TomatoExceptionCode.PASSWORD_INCORRECT_IN_MYPAGE);
    }

    // 2. 새 비밀번호와 확인 비밀번호가 일치하는지 확인
    if (!request.getNewPassword().equals(request.getConfirmPassword())) {
      throw new TomatoException(TomatoExceptionCode.PASSWORD_MISMATCH_IN_MYPAGE);
    }

    user.setPassword(passwordEncoder.encode(request.getNewPassword()));

    return PasswordUpdatedResponse.builder()
            .message("비밀번호가 성공적으로 변경되었습니다.")
            .build();
  }

}
