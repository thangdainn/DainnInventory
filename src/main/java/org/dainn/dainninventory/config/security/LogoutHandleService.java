package org.dainn.dainninventory.config.security;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.dainn.dainninventory.filter.JwtProvider;
import org.dainn.dainninventory.repository.ITokenRepository;
import org.dainn.dainninventory.service.IBaseRedisService;
import org.dainn.dainninventory.utils.JwtUtil;
import org.dainn.dainninventory.utils.constant.RedisConstant;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;

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
        System.out.println("key = " + key);
        baseRedisService.set(key, uuid);

        Cookie[] cookies = request.getCookies();
        String refreshToken = Arrays.stream(cookies)
                .filter(cookie -> cookie.getName().equals("refresh_token"))
                .map(Cookie::getValue)
                .findFirst()
                .orElse(null);
        if (refreshToken == null) {
            return;
        }
        tokenRepository.deleteByRefreshToken(refreshToken);
        Cookie cookie = new Cookie("refresh_token", null);
        cookie.setMaxAge(0);
        response.addCookie(cookie);
    }
}
