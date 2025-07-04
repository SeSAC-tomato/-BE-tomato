package com.exam.tomatoback.auth.service;

import com.exam.tomatoback.auth.model.VerityType;
import com.exam.tomatoback.infrastructure.exception.TomatoException;
import com.exam.tomatoback.infrastructure.exception.TomatoExceptionCode;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class MailServiceImpl implements MailService {
  private final JavaMailSender mailSender;
  private final UserVerifyService userVerifyService;

  @Override
  public void sendEmailVerify(String email, String nickname) {
    String token = UUID.randomUUID().toString();
    String verifyUrl = "http://localhost:3000/verify/email?token=" + token + "&email=" + email;
    try {
      MimeMessage message = mailSender.createMimeMessage();
      MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
      helper.setTo(email);
      helper.setSubject("[토마토] 이메일 인증을 완료해주세요 🍅");
      helper.setFrom("sesac.tomato@gmail.com");

      String htmlContent = """
              <html>
              <body style="font-family: Arial, sans-serif; background-color: #f9f9f9; padding: 20px;">
                <div style="max-width: 600px; margin: auto; background-color: #ffffff; padding: 30px; border-radius: 10px;">
                  <h2 style="color: #ff4d4f;">🍅 토마토 이메일 인증</h2>
                  <p><strong>%s</strong>님, 환영합니다! 아래 버튼을 눌러 이메일 인증을 완료해 주세요.</p>
                  <div style="text-align: center; margin: 30px 0;">
                    <a href="%s" style="background-color: #ff4d4f; color: #fff; padding: 12px 24px; border-radius: 6px; text-decoration: none; font-weight: bold;">
                      이메일 인증하기
                    </a>
                  </div>
                  <p style="font-size: 14px; color: #555;">해당 링크는 24시간 동안만 유효합니다.</p>
                </div>
              </body>
              </html>
          """.formatted(nickname, verifyUrl);

      helper.setText(htmlContent, true);
      mailSender.send(message);

      userVerifyService.save(email, token, VerityType.EMAIL);
    } catch (MessagingException e) {
      throw new TomatoException(TomatoExceptionCode.EMAIL_SEND_FAILED);
    }
  }
}
