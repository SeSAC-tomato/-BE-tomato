package com.exam.tomatoback.web.dto.auth.request;

public interface PasswordMatchable {
  String getPassword();

  String getPasswordConfirm();
}
