package org.dainn.dainninventory.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.dainn.dainninventory.config.endpoint.Endpoint;
import org.dainn.dainninventory.dto.auth.*;
import org.dainn.dainninventory.dto.device.DeviceInfoDTO;
import org.dainn.dainninventory.service.IAuthService;
import org.dainn.dainninventory.service.IOtpService;
import org.dainn.dainninventory.service.ITokenService;
import org.dainn.dainninventory.service.IUserService;
import org.dainn.dainninventory.utils.enums.Provider;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(Endpoint.Auth.BASE)
@RequiredArgsConstructor
public class AuthController {
    private final IAuthService authService;
    private final IUserService userService;
    private final ITokenService tokenService;
    private final IOtpService otpService;

    @PostMapping(Endpoint.Auth.LOGIN)
    public ResponseEntity<?> login(@Valid @RequestBody LoginDTO dto, HttpServletRequest request, HttpServletResponse response) {
        return ResponseEntity.ok(authService.login(dto, request, response));
    }

    @PostMapping(Endpoint.Auth.REGISTER)
    public ResponseEntity<?> register(@Valid @RequestBody RegisterDTO request) {
        return ResponseEntity.ok(authService.register(request));
    }

    @PostMapping(Endpoint.Auth.REFRESH_TOKEN)
    public ResponseEntity<?> refresh(HttpServletRequest request, HttpServletResponse response) {
        return ResponseEntity.ok(tokenService.handleRefreshToken(request, response));
    }

    @PostMapping(Endpoint.Auth.LOGIN_GOOGLE)
    public ResponseEntity<?> googleLogin(HttpServletRequest request, HttpServletResponse response){
        return ResponseEntity.ok(authService.loginGoogle(request, response));
    }

    @PostMapping(Endpoint.Auth.SEND_OTP)
    public ResponseEntity<?> sendOtp(@RequestBody OtpDTO dto) {
        otpService.sendOtp(dto.getEmail());
        return ResponseEntity.ok().build();
    }

    @PostMapping(Endpoint.Auth.VERIFY_OTP)
    public ResponseEntity<?> verifyOtp(@RequestBody OtpDTO dto) {
        return ResponseEntity.ok(otpService.verifyOtp(dto));
    }

    @PostMapping(Endpoint.Auth.CHECK_EMAIL_EXISTS)
    public ResponseEntity<?> checkEmailExists(@RequestBody OtpDTO dto) {
        return ResponseEntity.ok(userService.checkEmailAndProvider(dto.getEmail(), Provider.local));
    }

    @PostMapping(Endpoint.Auth.CHECK_PASSWORD)
    public ResponseEntity<?> checkPassword(@RequestBody ResetPassword dto) {
        return ResponseEntity.ok(authService.checkPassword(dto));
    }

    @PostMapping(Endpoint.Auth.RESET_PASSWORD)
    public ResponseEntity<?> resetPassword(@RequestBody ResetPassword dto, HttpServletRequest request) {
        authService.resetPassword(dto, request);
        return ResponseEntity.ok().build();
    }

    @PostMapping(Endpoint.Auth.FORGOT_PASSWORD)
    public ResponseEntity<?> forgotPassword(@RequestBody ResetPassword dto) {
        authService.forgotPassword(dto);
        return ResponseEntity.ok().build();
    }


}
