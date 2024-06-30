package org.dainn.dainninventory.service;

import jakarta.servlet.http.HttpServletResponse;
import org.dainn.dainninventory.controller.response.JwtResponse;
import org.dainn.dainninventory.dto.TokenDTO;

public interface ITokenService {
    TokenDTO insert(TokenDTO dto);
    JwtResponse handleRefreshToken(String refreshToken, HttpServletResponse response);
    void deleteByUserIdAndDeviceInfo(Integer userId, String deviceInfo);
    TokenDTO findByRefreshToken(String refreshToken);

}
