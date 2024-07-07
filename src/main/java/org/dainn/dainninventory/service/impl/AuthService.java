package org.dainn.dainninventory.service.impl;

import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.dainn.dainninventory.controller.request.LoginRequest;
import org.dainn.dainninventory.controller.request.Oauth2Request;
import org.dainn.dainninventory.controller.request.RegisterRequest;
import org.dainn.dainninventory.controller.response.JwtResponse;
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
import org.dainn.dainninventory.utils.constant.Oauth2Constant;
import org.dainn.dainninventory.utils.constant.RoleConstant;
import org.dainn.dainninventory.utils.enums.Provider;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Date;
import java.util.List;
import java.util.Map;
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
    private final RestTemplate restTemplate;

    @Override
    public JwtResponse login(LoginRequest request, HttpServletResponse response) {
        UserEntity userEntity = userRepository.findByEmailAndProviderAndStatus(request.getEmail(), Provider.local, 1)
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
    public UserDTO register(@Valid RegisterRequest request) {
        return userService.insert(userMapper.toUserRequest(request));
    }

    @Override
    public JwtResponse loginOauth2(String authorizationHeader, Oauth2Request oauth2Request, Provider provider, HttpServletResponse response) {
        String accessTokenOauth2 = authorizationHeader.substring(7);

        String userInfoEndpoint = Oauth2Constant.GOOGLE_USER_ENDPOINT;
        if (provider.equals(Provider.github)) {
            userInfoEndpoint = Oauth2Constant.GITHUB_USER_ENDPOINT;
        } else if (provider.equals(Provider.facebook)) {
            userInfoEndpoint = Oauth2Constant.FACEBOOK_USER_ENDPOINT;
        }

        Map<String, Object> userInfo = this.getUserInfo(accessTokenOauth2, userInfoEndpoint);
        String email = (String) userInfo.get("email");

        Optional<UserEntity> optional = userRepository.findByEmailAndProviderAndStatus(email, provider, 1);
        UserEntity userEntity = new UserEntity();
        if (optional.isEmpty()) {
            userEntity.setEmail(email);
            userEntity.setPassword(encoder.encode("123"));
            userEntity.setProvider(provider);
            RoleEntity roleEntity = roleRepository.findByName(RoleConstant.PREFIX_ROLE + "USER")
                    .orElseThrow(() -> new AppException(ErrorCode.ROLE_NOT_EXISTED));
            userEntity.setRoles(List.of(roleEntity));
        } else {
            userEntity = optional.get();
        }
        userEntity.setName((String) userInfo.get("name"));
        userEntity = userRepository.save(userEntity);
        String accessToken = jwtProvider.generateToken(userEntity.getEmail(), provider);
        String refreshToken = jwtProvider.generateRefreshToken();
        TokenDTO tokenDTO = TokenDTO.builder()
                .deviceInfo(oauth2Request.getDeviceInfo())
                .refreshToken(refreshToken)
                .refreshTokenExpirationDate(new Date(new Date().getTime() + JwtConstant.JWT_EXPIRATION_REFRESH))
                .userId(userEntity.getId())
                .build();
        tokenService.insert(tokenDTO);
        response.addCookie(CookieUtil.createRefreshTokenCookie(refreshToken));
        return new JwtResponse(accessToken);
    }

    private Map<String, Object> getUserInfo(String accessToken, String userInfoEndpoint) {
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(accessToken);

        HttpEntity<String> entity = new HttpEntity<>("", headers);

        ResponseEntity<Map> response = restTemplate.exchange(
                userInfoEndpoint, HttpMethod.GET, entity, Map.class);

        return response.getBody();
    }
}
