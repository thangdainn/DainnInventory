package org.dainn.dainninventory.controller;

import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.dainn.dainninventory.controller.request.LoginRequest;
import org.dainn.dainninventory.controller.request.Oauth2Request;
import org.dainn.dainninventory.controller.request.RegisterRequest;
import org.dainn.dainninventory.service.IAuthService;
import org.dainn.dainninventory.utils.enums.Provider;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {
    private final IAuthService authService;

    @PostMapping("/login")
    public ResponseEntity<?> login(@Valid @RequestBody LoginRequest request, HttpServletResponse response) {
        return ResponseEntity.ok(authService.login(request, response));
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@Valid @RequestBody RegisterRequest request) {
        return ResponseEntity.ok(authService.register(request));
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
