package com.stylefeng.guns.mail;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

public abstract class AbstractTextMailService implements IMailService{
    @Autowired
    private JavaMailSender javaMailSender;

    @Value("${spring.mail.username}")
    private String fromMail;

    @Override
    public final void sendMail(MimeMessage mimeMessage) {
        javaMailSender.send(mimeMessage);
    }

    public void sendTextMail(String[] toMails, String subject, String content) throws MessagingException {
        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true);
        helper.setFrom(fromMail);
        helper.setSubject(subject);
        helper.setText(content);
        helper.setTo(toMails);
        //todo   邮箱授权码已失效
//        this.sendMail(mimeMessage);
    }
}
