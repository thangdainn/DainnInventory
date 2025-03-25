package org.dainn.dainninventory.service.impl;

import lombok.RequiredArgsConstructor;
import org.dainn.dainninventory.dto.mail.MailData;
import org.dainn.dainninventory.dto.auth.OtpDTO;
import org.dainn.dainninventory.service.IBaseRedisService;
import org.dainn.dainninventory.service.IMailService;
import org.dainn.dainninventory.service.IOtpService;
import org.dainn.dainninventory.utils.constant.RedisConstant;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;

@Service
@RequiredArgsConstructor
public class OtpService implements IOtpService {
    private final IMailService mailService;
    private final IBaseRedisService baseRedisService;

    @Override
    public void sendOtp(String email) {
        String otp = generateOtp();
        saveOtp(email, otp);
        MailData mailData = MailData.builder()
                .to(email)
                .subject("Register account")
                .body("Your OTP is: " + otp)
                .build();
        mailService.sendEmail(mailData);
    }

    @Override
    public boolean verifyOtp(OtpDTO dto) {
        try {
            String key = RedisConstant.OTP_KEY_PREFIX + dto.getEmail();
            String savedOtp = baseRedisService.get(key).toString();
            if (savedOtp.equals(dto.getOtp())) {
                baseRedisService.delete(key);
                return true;
            }
            return false;
        } catch (Exception e) {
            return false;
        }

    }

    private String generateOtp() {
        SecureRandom random = new SecureRandom();
        int otp = random.nextInt(900000) + 100000;
        return String.valueOf(otp);
    }

    private void saveOtp(String email, String otp) {
        String key = RedisConstant.OTP_KEY_PREFIX + email;
        Object oldOtp = baseRedisService.get(key);
        if (oldOtp != null) {
            baseRedisService.delete(key);
        }
        baseRedisService.set(key, otp);
        baseRedisService.setTimeToValue(key, RedisConstant.OTP_EXPIRATION);
    }
}
