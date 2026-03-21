package com.stylefeng.guns.mail;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

public interface IMailService {
    void sendMail(MimeMessage mimeMessage);
    void sendTextMail(String[] toMails, String subject, String content) throws MessagingException;
}
