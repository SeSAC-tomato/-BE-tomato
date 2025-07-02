package com.exam.tomatoback.auth.service;

public interface MailService {
  void sendEmailVerify(String email, String nickname);
}
