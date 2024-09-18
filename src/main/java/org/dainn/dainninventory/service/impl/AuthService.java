package org.dainn.dainninventory.service.impl;

import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.gson.GsonFactory;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.dainn.dainninventory.controller.request.LoginRequest;
import org.dainn.dainninventory.controller.request.RegisterRequest;
import org.dainn.dainninventory.controller.response.JwtResponse;
import org.dainn.dainninventory.dto.OAuth2TokenDTO;
import org.dainn.dainninventory.dto.TokenDTO;
import org.dainn.dainninventory.dto.UserDTO;
import org.dainn.dainninventory.entity.RoleEntity;
import org.dainn.dainninventory.entity.UserEntity;
import org.dainn.dainninventory.exception.AppException;
import org.dainn.dainninventory.exception.ErrorCode;
import org.dainn.dainninventory.jwt.JwtProvider;
import org.dainn.dainninventory.mapper.IUserMapper;
import org.dainn.dainninventory.repository.IRoleRepository;
import org.dainn.dainninventory.repository.IUserRepository;
import org.dainn.dainninventory.service.IAuthService;
import org.dainn.dainninventory.service.ITokenService;
import org.dainn.dainninventory.service.IUserService;
import org.dainn.dainninventory.utils.CookieUtil;
import org.dainn.dainninventory.utils.constant.JwtConstant;
import org.dainn.dainninventory.utils.constant.RoleConstant;
import org.dainn.dainninventory.utils.enums.Provider;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AuthService implements IAuthService {
    private final IUserRepository userRepository;
    private final IRoleRepository roleRepository;
    private final IUserService userService;
    private final IUserMapper userMapper;
    private final ITokenService tokenService;
    private final PasswordEncoder encoder;
    private final JwtProvider jwtProvider;

    @Value("${google.clientId}")
    String googleClientId;

    @Override
    public JwtResponse login(LoginRequest request, HttpServletResponse response) {
        UserEntity userEntity = userRepository.findByEmailAndProviderAndStatus(request.getEmail(), Provider.local, 1)
                .orElseThrow(() -> new AppException(ErrorCode.EMAIL_IS_INCORRECT));
        if (!encoder.matches(request.getPassword(), userEntity.getPassword())) {
            throw new AppException(ErrorCode.PASSWORD_IS_INCORRECT);
        }
        String accessToken = jwtProvider.generateToken(userEntity.getEmail(), Provider.local);
        String refreshToken = jwtProvider.generateRefreshToken();
        TokenDTO tokenDTO = createTokenDTO(refreshToken, userEntity.getId(), request.getDeviceInfo());
        tokenService.insert(tokenDTO);
        response.addCookie(CookieUtil.createRefreshTokenCookie(refreshToken));
        return new JwtResponse(accessToken);
    }

    @Override
    public UserDTO register(@Valid RegisterRequest request) {
        return userService.insert(userMapper.toUserRequest(request));
    }

    @Override
    public JwtResponse loginGoogle(OAuth2TokenDTO oAuth2TokenDTO, HttpServletResponse response) {
        try {
            GoogleIdTokenVerifier verifier = new GoogleIdTokenVerifier.Builder(new NetHttpTransport(), new GsonFactory())
                    .setAudience(Collections.singleton(googleClientId))
                    .build();
            GoogleIdToken idToken = verifier.verify(oAuth2TokenDTO.getToken());
            GoogleIdToken.Payload payload = idToken.getPayload();
            String email = payload.getEmail();
            String name = (String) payload.get("name");
            Optional<UserEntity> optional = userRepository.findByEmailAndProviderAndStatus(email, Provider.google, 1);
            UserEntity userEntity = new UserEntity();
            if (optional.isEmpty()) {
                userEntity.setEmail(email);
                userEntity.setName(name);
                userEntity.setPassword(encoder.encode("dainn"));
                userEntity.setProvider(Provider.google);
                RoleEntity roleEntity = roleRepository.findByName(RoleConstant.PREFIX_ROLE + "USER")
                        .orElseThrow(() -> new AppException(ErrorCode.ROLE_NOT_EXISTED));
                userEntity.setRoles(List.of(roleEntity));
            } else {
                userEntity = optional.get();
            }
            userEntity = userRepository.save(userEntity);
            String accessToken = jwtProvider.generateToken(userEntity.getEmail(), Provider.google);
            String refreshToken = jwtProvider.generateRefreshToken();
            TokenDTO tokenDTO = createTokenDTO(refreshToken, userEntity.getId(), oAuth2TokenDTO.getDeviceInfo());
            tokenService.insert(tokenDTO);
            response.addCookie(CookieUtil.createRefreshTokenCookie(refreshToken));
            return new JwtResponse(accessToken);
        } catch (Exception e) {
            throw new AppException(ErrorCode.GOOGLE_LOGIN_FAILED);
        }
    }

    private TokenDTO createTokenDTO(String refreshToken, Integer userId, String deviceInfo) {
        return TokenDTO.builder()
                .deviceInfo(deviceInfo)
                .refreshToken(refreshToken)
                .refreshTokenExpirationDate(new Date(new Date().getTime() + JwtConstant.JWT_EXPIRATION_REFRESH))
                .userId(userId)
                .build();
    }
}
