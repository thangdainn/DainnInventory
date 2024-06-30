package org.dainn.dainninventory.service.impl;

import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.dainn.dainninventory.controller.request.LoginRequest;
import org.dainn.dainninventory.controller.response.JwtResponse;
import org.dainn.dainninventory.dto.TokenDTO;
import org.dainn.dainninventory.entity.UserEntity;
import org.dainn.dainninventory.exception.AppException;
import org.dainn.dainninventory.exception.ErrorCode;
import org.dainn.dainninventory.jwt.JwtProvider;
import org.dainn.dainninventory.repository.IUserRepository;
import org.dainn.dainninventory.service.IAuthService;
import org.dainn.dainninventory.service.ITokenService;
import org.dainn.dainninventory.utils.CookieUtil;
import org.dainn.dainninventory.utils.constant.JwtConstant;
import org.dainn.dainninventory.utils.enums.Provider;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
@RequiredArgsConstructor
public class AuthService implements IAuthService {
    private final IUserRepository userRepository;
    private final ITokenService tokenService;
    private final PasswordEncoder encoder;
    private final JwtProvider jwtProvider;

    @Override
    public JwtResponse login(LoginRequest request, HttpServletResponse response) {
        UserEntity userEntity = userRepository.findByEmailAndProvider(request.getEmail(), Provider.local)
                .orElseThrow(() -> new AppException(ErrorCode.EMAIL_IS_INCORRECT));
        if (!encoder.matches(request.getPassword(), userEntity.getPassword())) {
            throw new AppException(ErrorCode.PASSWORD_IS_INCORRECT);
        }
        String accessToken = jwtProvider.generateToken(userEntity.getEmail(), Provider.local);
        String refreshToken = jwtProvider.generateRefreshToken();
        TokenDTO tokenDTO = TokenDTO.builder()
                .deviceInfo(request.getDeviceInfo())
                .refreshToken(refreshToken)
                .refreshTokenExpirationDate(new Date(new Date().getTime() + JwtConstant.JWT_EXPIRATION_REFRESH))
                .userId(userEntity.getId())
                .build();
        tokenService.insert(tokenDTO);
        response.addCookie(CookieUtil.createRefreshTokenCookie(refreshToken));
        return new JwtResponse(accessToken);
    }

    @Override
    public void logout() {

    }

    @Override
    public void register(String username, String password) {

    }
}
