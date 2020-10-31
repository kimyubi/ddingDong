package com.mj.ddingdong.main.service;

import com.mj.ddingdong.account.domain.Account;
import com.mj.ddingdong.account.repository.AccountRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

@Service
@RequiredArgsConstructor
public class MainService {

    private final JavaMailSender javaMailSender;
    private final AccountRepository accountRepository;
    private final TemplateEngine templateEngine;


    public void sendFindAccountEmail(String email) {
        Account account = accountRepository.findByEmail(email);
        if(account == null){
            throw new IllegalArgumentException(email+"에 해당하는 사용자 정보가 없습니다.");
        }
        account.generatedToken();
        accountRepository.save(account);
        Context context = new Context();
        context.setVariable("title","계정 찾기");
        context.setVariable("name",account.getName());
        context.setVariable("signUpId",account.getSignUpId());
        context.setVariable("host","http://localhost:8080");
        context.setVariable("link","/update-password?token="+account.getToken());
        String message = templateEngine.process("emailForm",context);

        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        try {
            MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage, false, "UTF-8");
            mimeMessageHelper.setTo(email);
            mimeMessageHelper.setSubject("띵동 계정 찾기");
            mimeMessageHelper.setText(message,true);

            javaMailSender.send(mimeMessage);
        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }
}
