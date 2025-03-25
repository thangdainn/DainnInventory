package org.dainn.dainninventory.service.impl;

import lombok.RequiredArgsConstructor;
import org.dainn.dainninventory.dto.mail.MailData;
import org.dainn.dainninventory.service.IMailService;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MailService implements IMailService {
    private final JavaMailSender mailSender;
    @Override
    public void sendEmail(MailData data) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(data.getTo());
        message.setSubject(data.getSubject());
        message.setText(data.getBody());
        mailSender.send(message);
    }
}
