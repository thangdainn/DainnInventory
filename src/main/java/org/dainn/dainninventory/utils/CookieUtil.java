package org.dainn.dainninventory.utils;

import jakarta.servlet.http.Cookie;

public class CookieUtil {
    public static Cookie createRefreshTokenCookie(String refreshToken) {
        System.out.println("refreshToken: " + refreshToken);
        Cookie refreshTokenCookie = new Cookie("refresh_token", refreshToken);
        refreshTokenCookie.setHttpOnly(true);
        refreshTokenCookie.setSecure(false);
        refreshTokenCookie.setMaxAge(15 * 24 * 60 * 60);
        refreshTokenCookie.setPath("/");
        return refreshTokenCookie;
    }
}
