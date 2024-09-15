package org.dainn.dainninventory.controller;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import org.dainn.dainninventory.controller.request.LoginRequest;
import org.dainn.dainninventory.controller.request.Oauth2Request;
import org.dainn.dainninventory.controller.request.RegisterRequest;
import org.dainn.dainninventory.service.IAuthService;
import org.dainn.dainninventory.service.ITokenService;
import org.dainn.dainninventory.service.IUserService;
import org.dainn.dainninventory.utils.enums.Provider;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {
    private final IAuthService authService;
    private final IUserService userService;
    private final ITokenService tokenService;

    @GetMapping("/{id}")
    public ResponseEntity<?> get(@Min(1) @PathVariable Integer id) {
        return ResponseEntity.ok(userService.findById(id));
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@Valid @RequestBody LoginRequest request, HttpServletResponse response) {
        return ResponseEntity.ok(authService.login(request, response));
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@Valid @RequestBody RegisterRequest request) {
        return ResponseEntity.ok(authService.register(request));
    }

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

    @PostMapping("/login/oauth2/google")
    public ResponseEntity<?> handleGoogleLogin(@RequestHeader(HttpHeaders.AUTHORIZATION) String authorizationHeader,
                                               @Valid @RequestBody Oauth2Request request,
                                               HttpServletResponse response) {
        return ResponseEntity.ok(authService.loginOauth2(authorizationHeader, request, Provider.google, response));
    }
    @PostMapping("/login/oauth2/facebook")
    public ResponseEntity<?> handleFacebookLogin(@RequestHeader(HttpHeaders.AUTHORIZATION) String authorizationHeader,
                                               @Valid @RequestBody Oauth2Request request,
                                               HttpServletResponse response) {
        return ResponseEntity.ok(authService.loginOauth2(authorizationHeader, request, Provider.google, response));
    }
    @PostMapping("/login/oauth2/github")
    public ResponseEntity<?> handleGithubLogin(@RequestHeader(HttpHeaders.AUTHORIZATION) String authorizationHeader,
                                               @Valid @RequestBody Oauth2Request request,
                                               HttpServletResponse response) {
        return ResponseEntity.ok(authService.loginOauth2(authorizationHeader, request, Provider.google, response));
    }
}
