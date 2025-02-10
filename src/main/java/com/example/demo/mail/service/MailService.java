package com.example.demo.mail.service;

import com.example.demo.member.service.MemberService;
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

    private final MemberService memberService;

    @Value("${spring.mail.username}")
    private static String senderEmail;

    private static final String CHARACTERS = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";

    private static final Random random = new Random();

    public String createNumber() {
        StringBuilder key = new StringBuilder();

        for (int i = 0; i < 8; i++) {
            key.append(CHARACTERS.charAt(random.nextInt(CHARACTERS.length())));
        }

        return key.toString();
    }

    public String createProductCode() {
        StringBuilder key = new StringBuilder();

        for (int i = 0; i < 16; i++) {
            key.append(CHARACTERS.charAt(random.nextInt(CHARACTERS.length())));
        }
        return key.toString();
    }

    public MimeMessage createMimeMessage(String mail, String number) throws MessagingException {

        MimeMessage mimeMessage = mailSender.createMimeMessage();

        mimeMessage.setFrom(senderEmail);
        mimeMessage.setRecipients(MimeMessage.RecipientType.TO, mail);
        mimeMessage.setSubject("요청하신 이메일 인증번호입니다.");
        String body = "";
        body += "<h3>요청하신 인증번호입니다.</h3>";
        body += "<h1>" + number + "</h1>";
        body += "<h3>감사합니다.</h3>";
        mimeMessage.setText(body, "UTF-8", "html");

        return mimeMessage;
    }

    public MimeMessage createTempPasswordMessage(String mail, String tempPassword) throws MessagingException {
        MimeMessage mimeMessage = mailSender.createMimeMessage();

        mimeMessage.setFrom(senderEmail);
        mimeMessage.setRecipients(MimeMessage.RecipientType.TO, mail);
        mimeMessage.setSubject("요청하신 임시 비밀번호입니다.");
        String body = "";
        body += "<h3>요청하신 임시 비밀번호입니다.";
        body += "<h1>" + tempPassword + "</h1>";
        body += "<h3>로그인 후 비밀번호를 변경해주세요.</h3>";
        body += "<h3>감사합니다.</h3>";
        mimeMessage.setText(body, "UTF-8", "html");

        return mimeMessage;
    }

    public MimeMessage createProductCode(String mail, String code) throws MessagingException {
        MimeMessage mimeMessage = mailSender.createMimeMessage();

        mimeMessage.setFrom(senderEmail);
        mimeMessage.setRecipients(MimeMessage.RecipientType.TO, mail);
        mimeMessage.setSubject("구매하신 게임 코드입니다.");
        String body = "";
        body += "<h3>구매하신 게임 코드입니다.";
        body += "<h1>" + code + "</h1>";
        body += "<h3>등록하여 사용해 주십시오.</h3>";
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

    public void sendTempPassword(String sendEmail, String tempPassword) throws MessagingException {

        MimeMessage mimeMessage = createTempPasswordMessage(sendEmail, tempPassword);

        try {
            mailSender.send(mimeMessage);
        } catch (MailException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }

    }

    public void sendProductCode(String email) throws MessagingException {

        String code = createProductCode();

        MimeMessage mimeMessage = createProductCode(email, code);

        try {
            mailSender.send(mimeMessage);
        } catch (MailException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }

    }
}
