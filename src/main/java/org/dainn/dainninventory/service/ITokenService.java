package org.dainn.dainninventory.service;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.dainn.dainninventory.dto.response.JwtResponse;
import org.dainn.dainninventory.dto.token.TokenDTO;

public interface ITokenService {
    TokenDTO insert(TokenDTO dto);
    JwtResponse handleRefreshToken(HttpServletRequest request, HttpServletResponse response);
    void deleteByUserId(Integer userId);
    void deleteByUserIdAndNotIpAddress(Integer userId, String refreshToken);
}
