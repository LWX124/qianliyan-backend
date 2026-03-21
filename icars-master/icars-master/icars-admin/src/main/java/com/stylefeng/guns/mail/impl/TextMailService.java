package com.stylefeng.guns.mail.impl;

import com.stylefeng.guns.mail.AbstractTextMailService;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;


@Service
public class TextMailService extends AbstractTextMailService {

    @Override
    public void sendTextMail(String[] toMails, String subject, String content) throws MessagingException {
        super.sendTextMail(toMails, subject, content);
    }
}
