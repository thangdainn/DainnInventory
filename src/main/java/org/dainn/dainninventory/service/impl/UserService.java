package org.dainn.dainninventory.service.impl;

import com.fasterxml.jackson.core.type.TypeReference;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.dainn.dainninventory.controller.request.UserPageRequest;
import org.dainn.dainninventory.controller.request.UserRequest;
import org.dainn.dainninventory.dto.UserDTO;
import org.dainn.dainninventory.entity.RoleEntity;
import org.dainn.dainninventory.entity.UserEntity;
import org.dainn.dainninventory.exception.AppException;
import org.dainn.dainninventory.exception.ErrorCode;
import org.dainn.dainninventory.filter.JwtProvider;
import org.dainn.dainninventory.mapper.IUserMapper;
import org.dainn.dainninventory.repository.IRoleRepository;
import org.dainn.dainninventory.repository.IUserRepository;
import org.dainn.dainninventory.repository.specification.SearchOperation;
import org.dainn.dainninventory.repository.specification.SpecSearchCriteria;
import org.dainn.dainninventory.repository.specification.SpecificationBuilder;
import org.dainn.dainninventory.service.IBaseRedisService;
import org.dainn.dainninventory.service.IUserService;
import org.dainn.dainninventory.utils.Paging;
import org.dainn.dainninventory.utils.constant.RedisConstant;
import org.dainn.dainninventory.utils.constant.RoleConstant;
import org.dainn.dainninventory.utils.enums.Provider;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpHeaders;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class UserService implements IUserService {
    private final PasswordEncoder encoder;
    private final IUserRepository userRepository;
    private final IUserMapper userMapper;
    private final IRoleRepository roleRepository;
    private final IBaseRedisService baseRedisService;
    private final JwtProvider jwtProvider;

    @Transactional
    @Override
    public UserDTO insert(UserRequest userRequest) {
        UserDTO userDTO = userMapper.toDTO(userRequest);
        if (checkEmailAndProvider(userDTO.getEmail(), userDTO.getProvider())) {
            throw new AppException(ErrorCode.EMAIL_EXISTED);
        }
        UserEntity userEntity = userMapper.toEntity(userDTO);
        userEntity.setPassword(encoder.encode(userDTO.getPassword()));
        userEntity.setRoles(handleRoles(userDTO.getRolesName()));
        userDTO = userMapper.toDTO(userRepository.save(userEntity));

        String key = RedisConstant.USER_KEY_PREFIX + "::id:" + userDTO.getId();
        baseRedisService.setCache(key, userDTO);
        return userDTO;
    }


    @Transactional
    @Override
    public UserDTO update(UserRequest userRequest) {
        UserDTO userDTO = userMapper.toDTO(userRequest);
        UserEntity userOld = userRepository.findById(userDTO.getId())
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));
        if (!userOld.getEmail().equals(userDTO.getEmail())
                && checkEmailAndProvider(userDTO.getEmail(), userDTO.getProvider())) {
            throw new AppException(ErrorCode.EMAIL_EXISTED);
        }
        if (userDTO.getPassword().isBlank()) {
            userDTO.setPassword(userOld.getPassword());
        } else {
            userDTO.setPassword(encoder.encode(userDTO.getPassword()));
        }
        UserEntity userEntity = userMapper.updateEntity(userOld, userDTO);
        userEntity.setRoles(handleRoles(userDTO.getRolesName()));
        userDTO = userMapper.toDTO(userRepository.save(userEntity));

        String key = RedisConstant.USER_KEY_PREFIX + "::id:" + userDTO.getId();
        baseRedisService.flushDb();
        baseRedisService.setCache(key, userDTO);
        return userDTO;
    }
    @Override
    public boolean checkEmailAndProvider(String email, Provider provider) {
        return userRepository.existsByEmailAndProvider(email, provider);
    }

    private List<RoleEntity> handleRoles(List<String> rolesName) {
        List<RoleEntity> roles = new ArrayList<>();
        if (rolesName == null || rolesName.isEmpty()) {
            roles.add(roleRepository.findByName(RoleConstant.PREFIX_ROLE + "USER")
                    .orElseThrow(() -> new AppException(ErrorCode.ROLE_NOT_EXISTED)));
        } else {
            for (String name : rolesName) {
                RoleEntity role = roleRepository.findByName(name)
                        .orElseThrow(() -> new AppException(ErrorCode.ROLE_NOT_EXISTED));
                roles.add(role);
            }
        }
        return roles;
    }

    @Transactional
    @Override
    public void delete(List<Integer> ids) {
        userRepository.deleteAllByIdInBatchCustom(ids);
        baseRedisService.flushDb();
    }

    @Override
    public UserDTO findById(Integer id) {
        return userMapper.toDTO(userRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED)));
    }

    @Override
    public UserDTO findMyInfo(HttpServletRequest request) {
        String jwt = getJwtFromRequest(request);
        if (!StringUtils.hasText(jwt) || !jwtProvider.validateToken(jwt)) {
            throw new AppException(ErrorCode.INVALID_TOKEN);
        }
        Integer userId = jwtProvider.extractId(jwt);
        return userMapper.toDTO(userRepository.findById(userId)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED)));
    }

    private String getJwtFromRequest(HttpServletRequest request) {
        String bearerToken = request.getHeader(HttpHeaders.AUTHORIZATION);
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }
        return null;
    }

    @Override
    public UserDTO findByEmailAndProvider(String email, Provider provider) {
        return userMapper.toDTO(userRepository.findByEmailAndProviderAndStatus(email, provider, 1)
                    .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED)));
    }


    @Override
    public List<UserDTO> findAll() {
        String key = RedisConstant.USERS_KEY_PREFIX;
        List<UserDTO> list = baseRedisService.getCache(key, new TypeReference<>() {
        });
        if (list == null) {
            list = userRepository.findAll()
                    .stream().map(userMapper::toDTO).toList();
            baseRedisService.setCache(key, list);
        }
        return list;
    }


    @Override
    public List<UserDTO> findAll(Integer status) {
        String key = RedisConstant.USERS_KEY_PREFIX + "::status:" + status;
        List<UserDTO> list = baseRedisService.getCache(key, new TypeReference<>() {
        });
        if (list == null) {
            list = userRepository.findAllByStatus(status)
                    .stream().map(userMapper::toDTO).toList();
            baseRedisService.setCache(key, list);
        }
        return list;
    }

    @Override
    public Page<UserDTO> findWithSpec(UserPageRequest request) {
        String key = RedisConstant.USERS_KEY_PREFIX + "::page:" + request.getPage() + "::size:" + request.getSize()
                + "::sort:" + request.getSortBy() + "::dir:" + request.getSortDir() + "::keyword:" + request.getKeyword()
                + "::provider:" + request.getProvider() + "::status:" + request.getStatus() + "::roleId:" + request.getRoleId();
        Page<UserDTO> pageDTO = baseRedisService.getCache(key, new TypeReference<>() {
        });
        if (pageDTO != null) {
            return pageDTO;
        }
        SpecificationBuilder<UserEntity> builder = new SpecificationBuilder<>();
        Page<UserEntity> page;
        Specification<UserEntity> spec;
        if (StringUtils.hasText(request.getKeyword())) {
            builder.with("email", SearchOperation.CONTAINS, request.getKeyword(), true);
            builder.with("name", SearchOperation.CONTAINS, request.getKeyword(), true);
        }
        if (request.getProvider() != null) {
            builder.with("provider", SearchOperation.EQUALITY, request.getProvider(), false);
        }
        builder.with("status", SearchOperation.EQUALITY, request.getStatus(), false);
        spec = builder.build();
        if (request.getRoleId() != null) {
            List<SpecSearchCriteria> roleCriteria = new ArrayList<>();
            roleCriteria.add(new SpecSearchCriteria("id", SearchOperation.EQUALITY, request.getRoleId(), true));
            Specification<UserEntity> roleSpec = builder.joinTableWithCondition("roles", roleCriteria);
            spec = Specification.where(spec).and(roleSpec);
        }
        page = userRepository.findAll(Objects.requireNonNull(spec), Paging.getPageable(request));
        pageDTO = page.map(userMapper::toDTO);
        baseRedisService.setCache(key, pageDTO);
        return pageDTO;
    }
}
