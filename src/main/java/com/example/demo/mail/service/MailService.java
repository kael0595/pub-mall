package com.example.demo.mail.service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.util.Random;

@Service
@RequiredArgsConstructor
public class MailService {

    private final JavaMailSender mailSender;

    @Value("${spring.mail.username}")
    private static String senderEmail;

    public String createNumber() {
        Random random = new Random();

        StringBuilder key = new StringBuilder();

        for (int i = 0; i < 8; i++) {
            int index = random.nextInt(3);

            switch (index) {
                case 0 -> key.append((char) (random.nextInt(26) + 97));
                case 1 -> key.append((char) (random.nextInt(26) + 65));
                case 2 -> key.append((char) (random.nextInt(10) + '0'));
            }
        }

        return key.toString();
    }

    public MimeMessage createMimeMessage(String mail, String number) throws MessagingException {

        MimeMessage mimeMessage = mailSender.createMimeMessage();

        mimeMessage.setFrom(senderEmail);
        mimeMessage.setRecipients(MimeMessage.RecipientType.TO, mail);
        mimeMessage.setSubject("이메일 인증");
        String body = "";
        body += "<h3>요청하신 인증번호입니다.</h3>";
        body += "<h1>" + number + "</h1>";
        body += "<h3>감사합니다.</h3>";
        mimeMessage.setText(body, "UTF-8", "html");

        return mimeMessage;
    }

    public String sendSimpleMessage(String sendEmail) throws MessagingException {
        String number = createNumber();

        MimeMessage mimeMessage = createMimeMessage(sendEmail, number);

        try {
            mailSender.send(mimeMessage);
        } catch (MailException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }

        return number;
    }
}
