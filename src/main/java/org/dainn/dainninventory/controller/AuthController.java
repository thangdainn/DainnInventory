package org.dainn.dainninventory.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import org.dainn.dainninventory.controller.request.LoginRequest;
import org.dainn.dainninventory.controller.request.RegisterRequest;
import org.dainn.dainninventory.controller.response.JwtResponse;
import org.dainn.dainninventory.dto.DeviceInfoDTO;
import org.dainn.dainninventory.service.IAuthService;
import org.dainn.dainninventory.service.ITokenService;
import org.dainn.dainninventory.service.IUserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
        String refreshToken = tokenService.getRefreshTokenFromReq(request);
        return ResponseEntity.ok(tokenService.handleRefreshToken(refreshToken, response));
    }

    @PostMapping("/login/oauth2/google")
    public ResponseEntity<?> googleLogin(HttpServletRequest request, @RequestBody DeviceInfoDTO deviceInfo, HttpServletResponse response){
        return ResponseEntity.ok(authService.loginGoogle(request, deviceInfo, response));
    }

}
