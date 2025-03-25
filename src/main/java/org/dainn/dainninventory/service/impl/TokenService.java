package org.dainn.dainninventory.service.impl;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.dainn.dainninventory.dto.response.JwtResponse;
import org.dainn.dainninventory.dto.token.TokenDTO;
import org.dainn.dainninventory.entity.TokenEntity;
import org.dainn.dainninventory.exception.AppException;
import org.dainn.dainninventory.exception.ErrorCode;
import org.dainn.dainninventory.filter.JwtProvider;
import org.dainn.dainninventory.mapper.ITokenMapper;
import org.dainn.dainninventory.mapper.IUserMapper;
import org.dainn.dainninventory.repository.ITokenRepository;
import org.dainn.dainninventory.repository.IUserRepository;
import org.dainn.dainninventory.service.ITokenService;
import org.dainn.dainninventory.utils.CookieUtil;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.Date;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class TokenService implements ITokenService {
    private final ITokenRepository tokenRepository;
    private final IUserRepository userRepository;
    private final ITokenMapper tokenMapper;
    private final IUserMapper userMapper;
    private final JwtProvider jwtProvider;

    @Transactional
    @Override
    public TokenDTO insert(TokenDTO dto) {
        TokenEntity tokenEntity = tokenMapper.toEntity(dto);
        Optional<TokenEntity> oldToken = tokenRepository.findByUserIdAndIpAddress(dto.getUserId(), dto.getIpAddress());
        oldToken.ifPresent(entity -> tokenRepository.deleteById(entity.getId()));
        tokenEntity.setUser(userRepository.findById(dto.getUserId())
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED)));
        return tokenMapper.toDTO(tokenRepository.save(tokenEntity));
    }

    @Transactional
    @Override
    public JwtResponse handleRefreshToken(HttpServletRequest request, HttpServletResponse response) {
        String refreshToken = getRefreshTokenFromReq(request);
        TokenEntity tokenEntity = tokenRepository.findByRefreshToken(refreshToken)
                .orElseThrow(() -> new AppException(ErrorCode.REFRESH_NOT_EXISTED));
        if (tokenEntity.getRefreshTokenExpirationDate().before(new Date())) {
            tokenRepository.deleteById(tokenEntity.getId());
            throw new AppException(ErrorCode.REFRESH_TOKEN_EXPIRED);
        }
        String accessToken = jwtProvider.generateToken(userMapper.toDTO(tokenEntity.getUser()));
        String refreshTokenNew = jwtProvider.generateRefreshToken();
        tokenRepository.updateRefreshToken(refreshTokenNew, tokenEntity.getId());
        response.addCookie(CookieUtil.createRefreshTokenCookie(refreshTokenNew));
        return new JwtResponse(accessToken);
    }

    private String getRefreshTokenFromReq(HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();
        return Arrays.stream(cookies)
                .filter(cookie -> cookie.getName().equals("refresh_token"))
                .map(Cookie::getValue)
                .findFirst()
                .orElse(null);
    }

    @Override
    @Transactional
    public void deleteByUserId(Integer userId) {
        tokenRepository.deleteByUser_Id(userId);
    }

    @Transactional
    @Override
    public void deleteByUserIdAndNotIpAddress(Integer userId, String ipAddress) {
        tokenRepository.deleteByUserIdAndNotIpAddress(userId, ipAddress);
    }
}
