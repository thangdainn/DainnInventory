package org.dainn.dainninventory.service;

import org.dainn.dainninventory.dto.mail.MailData;

public interface IMailService {
    void sendEmail(MailData data);
}
