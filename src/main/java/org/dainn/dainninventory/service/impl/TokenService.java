package org.dainn.dainninventory.service.impl;

import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.dainn.dainninventory.controller.response.JwtResponse;
import org.dainn.dainninventory.dto.TokenDTO;
import org.dainn.dainninventory.entity.TokenEntity;
import org.dainn.dainninventory.exception.AppException;
import org.dainn.dainninventory.exception.ErrorCode;
import org.dainn.dainninventory.jwt.JwtProvider;
import org.dainn.dainninventory.mapper.ITokenMapper;
import org.dainn.dainninventory.repository.ITokenRepository;
import org.dainn.dainninventory.repository.IUserRepository;
import org.dainn.dainninventory.service.ITokenService;
import org.dainn.dainninventory.utils.CookieUtil;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

@Service
@RequiredArgsConstructor
public class TokenService implements ITokenService {
    private final ITokenRepository tokenRepository;
    private final IUserRepository userRepository;
    private final ITokenMapper tokenMapper;
    private final JwtProvider jwtProvider;

    @Transactional
    @Override
    public TokenDTO insert(TokenDTO dto) {
        TokenEntity tokenEntity = tokenMapper.toEntity(dto);
        tokenEntity.setUser(userRepository.findById(dto.getUserId())
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED)));
        return tokenMapper.toDTO(tokenRepository.save(tokenEntity));
    }

    @Transactional
    @Override
    public JwtResponse handleRefreshToken(String refreshToken, HttpServletResponse response) {
        TokenEntity tokenEntity = tokenRepository.findByRefreshToken(refreshToken)
                .orElseThrow(() -> new AppException(ErrorCode.REFRESH_NOT_EXISTED));
        if (tokenEntity.getRefreshTokenExpirationDate().before(new Date())) {
            tokenRepository.deleteById(tokenEntity.getId());
            throw new AppException(ErrorCode.REFRESH_TOKEN_EXPIRED);
        }
        String accessToken = jwtProvider.generateToken(tokenEntity.getUser().getEmail(), tokenEntity.getUser().getProvider());
        String refreshTokenNew = jwtProvider.generateRefreshToken();
        tokenRepository.updateRefreshToken(refreshTokenNew, tokenEntity.getId());
        response.addCookie(CookieUtil.createRefreshTokenCookie(refreshTokenNew));
        return new JwtResponse(accessToken);
    }

    @Transactional
    @Override
    public void deleteByUserIdAndDeviceInfo(Integer userId, String deviceInfo) {
        tokenRepository.deleteByUser_IdAndDeviceInfo(userId, deviceInfo);
    }

    @Override
    public TokenDTO findByRefreshToken(String refreshToken) {
        return tokenMapper.toDTO(tokenRepository.findByRefreshToken(refreshToken)
                .orElseThrow(() -> new AppException(ErrorCode.REFRESH_NOT_EXISTED)));
    }
}
