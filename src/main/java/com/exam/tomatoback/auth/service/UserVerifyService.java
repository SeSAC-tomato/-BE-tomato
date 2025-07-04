package com.exam.tomatoback.auth.service;

import com.exam.tomatoback.auth.model.VerityType;

public interface UserVerifyService {
  void save(String email, String token, VerityType type);
}
