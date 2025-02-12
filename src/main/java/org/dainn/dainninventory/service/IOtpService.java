package org.dainn.dainninventory.service;

import org.dainn.dainninventory.dto.auth.OtpDTO;

public interface IOtpService {
    void sendOtp(String email);
    boolean verifyOtp(OtpDTO dto);
}
