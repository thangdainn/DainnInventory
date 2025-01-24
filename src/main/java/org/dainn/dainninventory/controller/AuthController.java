package org.dainn.dainninventory.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.dainn.dainninventory.config.endpoint.Endpoint;
import org.dainn.dainninventory.controller.request.LoginRequest;
import org.dainn.dainninventory.controller.request.RegisterRequest;
import org.dainn.dainninventory.dto.DeviceInfoDTO;
import org.dainn.dainninventory.service.IAuthService;
import org.dainn.dainninventory.service.ITokenService;
import org.dainn.dainninventory.service.IUserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(Endpoint.Auth.BASE)
@RequiredArgsConstructor
public class AuthController {
    private final IAuthService authService;
    private final IUserService userService;
    private final ITokenService tokenService;

    @GetMapping(Endpoint.Auth.ME)
    public ResponseEntity<?> getMe(HttpServletRequest request) {
        return ResponseEntity.ok(userService.findMyInfo(request));
    }

    @PostMapping(Endpoint.Auth.LOGIN)
    public ResponseEntity<?> login(@Valid @RequestBody LoginRequest request, HttpServletResponse response) {
        return ResponseEntity.ok(authService.login(request, response));
    }

    @PostMapping(Endpoint.Auth.REGISTER)
    public ResponseEntity<?> register(@Valid @RequestBody RegisterRequest request) {
        return ResponseEntity.ok(authService.register(request));
    }

    @PostMapping(Endpoint.Auth.REFRESH_TOKEN)
    public ResponseEntity<?> refresh(HttpServletRequest request, HttpServletResponse response) {
        return ResponseEntity.ok(tokenService.handleRefreshToken(request, response));
    }

    @PostMapping(Endpoint.Auth.LOGIN_GOOGLE)
    public ResponseEntity<?> googleLogin(HttpServletRequest request, @RequestBody DeviceInfoDTO deviceInfo, HttpServletResponse response){
        return ResponseEntity.ok(authService.loginGoogle(request, deviceInfo, response));
    }

}
