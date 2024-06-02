package org.dainn.dainninventory.service.impl;

import lombok.RequiredArgsConstructor;
import org.dainn.dainninventory.controller.request.UserPageRequest;
import org.dainn.dainninventory.controller.response.PageResponse;
import org.dainn.dainninventory.controller.response.UserResponse;
import org.dainn.dainninventory.dto.UserDTO;
import org.dainn.dainninventory.entity.RoleEntity;
import org.dainn.dainninventory.entity.UserEntity;
import org.dainn.dainninventory.exception.AppException;
import org.dainn.dainninventory.exception.ErrorCode;
import org.dainn.dainninventory.mapper.IUserMapper;
import org.dainn.dainninventory.repository.IRoleRepository;
import org.dainn.dainninventory.repository.IUserRepository;
import org.dainn.dainninventory.service.IUserService;
import org.dainn.dainninventory.utils.ProviderId;
import org.springframework.data.domain.*;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService implements IUserService {
    private final PasswordEncoder encoder;
    private final IUserRepository userRepository;
    private final IUserMapper userMapper;
    private final IRoleRepository roleRepository;

    @Transactional
    @Override
    public UserResponse save(UserDTO userDTO) {
        UserEntity userEntity;
        if (userDTO.getId() != null) {
            UserEntity userOld = userRepository.findById(userDTO.getId())
                    .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));
            if (userDTO.getPassword().isBlank()) {
                userDTO.setPassword(userOld.getPassword());
            } else {
                userDTO.setPassword(encoder.encode(userDTO.getPassword()));
            }
            userEntity = userMapper.updateEntity(userOld, userDTO);
        } else {
            userEntity = userMapper.toEntity(userDTO);
            userEntity.setPassword(encoder.encode(userDTO.getPassword()));
        }
        List<RoleEntity> roles = new ArrayList<>();
        if (userDTO.getRolesName().isEmpty()){
            roles.add(roleRepository.findByName("ROLE_USER")
                    .orElseThrow(() -> new AppException(ErrorCode.ROLE_NOT_EXISTED)));
        } else {
            for (String name : userDTO.getRolesName()) {
                RoleEntity role = roleRepository.findByName(name)
                        .orElseThrow(() -> new AppException(ErrorCode.ROLE_NOT_EXISTED));
                roles.add(role);
            }
        }
        userEntity.setRoles(roles);
        return userMapper.toResponse(userRepository.save(userEntity));
    }

    @Transactional
    @Override
    public void delete(List<Integer> ids) {
        userRepository.deleteAllByIdInBatchCustom(ids);
    }

    @Override
    public UserResponse findById(Integer id) {
        return userRepository.findById(id)
                .map(userMapper::toResponse).orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));
    }

    @Override
    public UserDTO findByEmail(String email) {
        return null;
    }

    @Override
    public List<UserResponse> findAll() {
        return userRepository.findAll().stream().map(userMapper::toResponse).toList();
    }

    @Override
    public Page<UserResponse> findAll(Pageable pageable) {
        Page<UserEntity> page = userRepository.findAll(pageable);
        return new PageImpl<>(page.getContent().stream()
                .map(userMapper::toResponse).toList(), page.getPageable(), page.getTotalElements());
    }

    @Override
    public Page<UserResponse> findByEmailContaining(String email, Pageable pageable) {
        Page<UserEntity> page = userRepository.findByEmailContaining(email, pageable);
        return new PageImpl<>(page.getContent().stream()
                .map(userMapper::toResponse).toList(), page.getPageable(), page.getTotalElements());
    }

    @Override
    public UserDTO findByEmailAndProviderId(String email, ProviderId providerId) {
        return userRepository.findByEmailAndProviderId(email, providerId)
                .map(userMapper::toDTO)
                .orElse(null);
    }
}
