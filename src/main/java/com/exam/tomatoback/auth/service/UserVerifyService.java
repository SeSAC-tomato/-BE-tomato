package com.exam.tomatoback.auth.service;

import com.exam.tomatoback.auth.model.UserVerify;
import com.exam.tomatoback.auth.model.VerityType;
import com.exam.tomatoback.web.dto.auth.request.VerifyRequest;

public interface UserVerifyService {
  void save(String email, String token, VerityType type);

  UserVerify verify(VerifyRequest request, Boolean expiredCheck);

  void delete(UserVerify verify);
}
