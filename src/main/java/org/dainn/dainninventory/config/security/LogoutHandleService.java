package org.dainn.dainninventory.config.security;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.dainn.dainninventory.filter.JwtProvider;
import org.dainn.dainninventory.repository.ITokenRepository;
import org.dainn.dainninventory.service.IBaseRedisService;
import org.dainn.dainninventory.utils.CookieUtil;
import org.dainn.dainninventory.utils.JwtUtil;
import org.dainn.dainninventory.utils.constant.RedisConstant;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class LogoutHandleService implements LogoutHandler {
    private final ITokenRepository tokenRepository;
    private final IBaseRedisService baseRedisService;
    private final JwtProvider jwtProvider;

    @Transactional
    @Override
    public void logout(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {
        String jwt = JwtUtil.getJwtFromRequest(request);
        String uuid = jwtProvider.extractUUID(jwt);
        String key = RedisConstant.BLACKLISTING + ":" + uuid;
        baseRedisService.set(key, uuid);

        String refreshToken = CookieUtil.getRefreshToken(request);
        if (refreshToken == null) {
            return;
        }
        tokenRepository.deleteByRefreshToken(refreshToken);
        Cookie cookie = new Cookie("refresh_token", null);
        cookie.setMaxAge(0);
        response.addCookie(cookie);
    }
}
