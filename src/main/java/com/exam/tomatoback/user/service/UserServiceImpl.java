package com.exam.tomatoback.user.service;

import com.exam.tomatoback.auth.service.KakaoLocalService;
import com.exam.tomatoback.infrastructure.exception.TomatoException;
import com.exam.tomatoback.infrastructure.exception.TomatoExceptionCode;
import com.exam.tomatoback.user.model.Address;
import com.exam.tomatoback.user.model.User;
import com.exam.tomatoback.user.repository.UserRepository;
import com.exam.tomatoback.web.dto.auth.response.KakaoAddressResponse;
import com.exam.tomatoback.web.dto.mypage.request.PasswordUpdateRequest;
import com.exam.tomatoback.web.dto.mypage.request.UserUpdateRequest;
import com.exam.tomatoback.web.dto.mypage.response.PasswordUpdatedResponse;
import com.exam.tomatoback.web.dto.mypage.response.UserInfoResponse;
import com.exam.tomatoback.web.dto.mypage.response.UserResponse;
import com.exam.tomatoback.web.dto.mypage.response.UserUpdatedResponse;
import lombok.RequiredArgsConstructor;
import org.locationtech.jts.geom.Point;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.PrecisionModel;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.exam.tomatoback.infrastructure.util.GeometryUtil;
import java.util.Optional;

import static com.exam.tomatoback.infrastructure.exception.TomatoExceptionCode.USER_NOT_FOUND_IN_MYPAGE;
import com.exam.tomatoback.infrastructure.util.GeometryUtil;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
  private final UserRepository repository;
  private final PasswordEncoder passwordEncoder;
  private final KakaoLocalService kakaoLocalService;

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

//    User certifiedUserId = getCurrentUser();
//
//    if (certifiedUserId.equals(userId)) { throw new TomatoException(TomatoExceptionCode.USER_MISMATCH_IN_MYPAGE);}
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
//  @Override
//  public User getCurrentUser() {
//    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
//    if(authentication == null || !authentication.isAuthenticated()) {
//      throw new TomatoException(TomatoExceptionCode.UNABLE_AUTH_INFO);
//    }
//    Object principal = authentication.getPrincipal();
//    if(principal instanceof UserDetails userDetails) {
//      String username = userDetails.getUsername();
//      return repository.findByEmail(username).orElseThrow(
//              () -> new TomatoException(TomatoExceptionCode.USER_NOT_FOUND)
//      );
//    } else {
//      throw new TomatoException(TomatoExceptionCode.UNABLE_AUTH_INFO);
//    }
//  }

  @Override
  public User getUserByUserDetails(UserDetails userDetails) {
    return getOptionalUser(userDetails.getUsername()).orElseThrow(
            () -> new TomatoException(TomatoExceptionCode.USER_NOT_FOUND)
    );
  }

  // 마이페이지. userId로 유저 정보 변경
  @Transactional
  @Override
  public UserUpdatedResponse updateUserById(Long userId, UserUpdateRequest request) {
    User user = repository.findById(userId)
        .orElseThrow(() -> new TomatoException(TomatoExceptionCode.USER_NOT_FOUND_IN_MYPAGE));

    // 닉네임 중복 체크
    if (!user.getNickname().equals(request.getNickname()) && existsByNickname(request.getNickname())) {
        throw new TomatoException(TomatoExceptionCode.DUPLICATE_NICKNAME_IN_MYPAGE);
    }
    user.setNickname(request.getNickname());

    // 주소 정보 업데이트
    KakaoAddressResponse kakaoResponse = kakaoLocalService.searchAddress(request.getAddress());
    if (kakaoResponse.getDocuments() == null || kakaoResponse.getDocuments().isEmpty()) {
        throw new TomatoException(TomatoExceptionCode.ADDRESS_NOT_FOUND);
    }
    KakaoAddressResponse.Document document = kakaoResponse.getDocuments().get(0);
    KakaoAddressResponse.Address addr = document.getAddress();

    String sido = addr.getRegion_1depth_name();
    String sigungu = addr.getRegion_2depth_name();
    String dong = addr.getRegion_3depth_name();
    Double x = addr.getX();
    Double y = addr.getY();
    GeometryFactory geometryFactory = new GeometryFactory(new PrecisionModel(), 4326);
    Point point = geometryFactory.createPoint(new Coordinate(x, y));

    // Address 엔티티에 값 세팅
    Address address = user.getAddress();
    if (address != null) {
        address.setAddress(request.getAddress());
        address.setSido(sido);
        address.setSigungu(sigungu);
        address.setDong(dong);
        address.setPoint(point);
    } else {
        Address newAddress = Address.builder()
            .user(user)
            .address(request.getAddress())
            .sido(sido)
            .sigungu(sigungu)
            .dong(dong)
            .point(point)
            .build();
        user.setAddress(newAddress);
    }

    return new UserUpdatedResponse(user.getNickname(), user.getAddress() != null ? user.getAddress().getAddress() : null);
  }

  // 마이페이지. userId로 비밀번호 변경
  @Transactional
  @Override
  public PasswordUpdatedResponse updatePasswordById(Long userId, PasswordUpdateRequest request) {
//    User certifiedUserId = getCurrentUser();
//
//    if (!certifiedUserId.equals(userId)) { throw new TomatoException(TomatoExceptionCode.USER_MISMATCH_IN_MYPAGE);}

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


  public User getUserByUserId(long userId) {
    return repository.findById(userId).orElseThrow(() -> new TomatoException(TomatoExceptionCode.USER_NOT_FOUND));
  }
  // UserService.java
  @Override
  public UserInfoResponse getCurrentUserInfo() {
    User user = getCurrentUser(); // 내부 로직
    return UserInfoResponse.from(user); // DTO로 변환까지 여기서 수행
  }
}
