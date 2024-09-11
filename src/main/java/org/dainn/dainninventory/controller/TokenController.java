package org.dainn.dainninventory.controller;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.dainn.dainninventory.service.ITokenService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;

@RestController
@RequiredArgsConstructor
public class TokenController {
    private final ITokenService tokenService;

    @PostMapping("/refresh-token")
    public ResponseEntity<?> refresh(HttpServletRequest request, HttpServletResponse response) {
        Cookie[] cookies = request.getCookies();
        String refreshToken = Arrays.stream(cookies)
                .filter(cookie -> cookie.getName().equals("refresh_token"))
                .map(Cookie::getValue)
                .findFirst()
                .orElse(null);
        if (refreshToken == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        return ResponseEntity.ok(tokenService.handleRefreshToken(refreshToken, response));
    }


}
