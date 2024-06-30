package org.dainn.dainninventory.utils;

import jakarta.servlet.http.Cookie;

public class CookieUtil {
    public static Cookie createRefreshTokenCookie(String refreshToken) {
        Cookie refreshTokenCookie = new Cookie("refresh_token", refreshToken);
        refreshTokenCookie.setHttpOnly(true);
        refreshTokenCookie.setSecure(true);
        refreshTokenCookie.setMaxAge(15 * 24 * 60 * 60);
        return refreshTokenCookie;
    }
}
