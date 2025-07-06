package com.exam.tomatoback.auth.service;

public interface MailService {
  void sendEmailVerify(String email, String nickname);

  void sendPasswordVerify(String email, String nickname);
}
