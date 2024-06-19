package org.dainn.dainninventory.service.impl;

import lombok.RequiredArgsConstructor;
import org.dainn.dainninventory.controller.request.UserPageRequest;
import org.dainn.dainninventory.controller.request.UserRequest;
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
    public UserDTO save(UserRequest userRequest) {
        UserDTO userDTO = userMapper.toDTO(userRequest);
        UserEntity userEntity;
        if (userDTO.getId() != null) {
            UserEntity userOld = userRepository.findById(userDTO.getId())
                    .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));
            if (!userOld.getEmail().equals(userDTO.getEmail()) && checkEmailAndProvider(userDTO.getEmail(), userDTO.getProviderId())) {
                throw new AppException(ErrorCode.EMAIL_EXISTED);
            }
            if (userDTO.getPassword().isBlank()) {
                userDTO.setPassword(userOld.getPassword());
            } else {
                userDTO.setPassword(encoder.encode(userDTO.getPassword()));
            }
            userEntity = userMapper.updateEntity(userOld, userDTO);
        } else {
            if (checkEmailAndProvider(userDTO.getEmail(), userDTO.getProviderId())) {
                throw new AppException(ErrorCode.EMAIL_EXISTED);
            }
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
        return userMapper.toDTO(userRepository.save(userEntity));
    }

    boolean checkEmailAndProvider(String email, ProviderId providerId) {
        return userRepository.existsByEmailAndProviderId(email, providerId);
    }

    @Transactional
    @Override
    public void delete(List<Integer> ids) {
        userRepository.deleteAllByIdInBatchCustom(ids);
    }

    @Override
    public UserDTO findById(Integer id) {
        return userRepository.findById(id)
                .map(userMapper::toDTO).orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));
    }

    @Override
    public UserDTO findByEmail(String email) {
        return null;
    }

    @Override
    public List<UserDTO> findAll() {
        return userRepository.findAll().stream().map(userMapper::toDTO).toList();
    }

    @Override
    public Page<UserDTO> findAll(Pageable pageable) {
        Page<UserEntity> page = userRepository.findAll(pageable);
        return new PageImpl<>(page.getContent().stream()
                .map(userMapper::toDTO).toList(), page.getPageable(), page.getTotalElements());
    }

    @Override
    public Page<UserDTO> findAll(UserPageRequest request) {
        Sort sort;
        if (request.getSortBy() != null && !request.getSortBy().isBlank()) {
            sort = request.getSortBy().equalsIgnoreCase(Sort.Direction.ASC.name())
                    ? Sort.by(request.getSortBy()).ascending() : Sort.by(request.getSortBy()).descending();
        } else {
            sort = Sort.unsorted();
        }
        Pageable pageable = PageRequest.of(request.getPage(), request.getSize(), sort);
        Page<UserEntity> entityPage;
        if (request.getKeyword() != null) {
            entityPage = userRepository.findByEmailContaining(request.getKeyword(), pageable);
        } else {
            entityPage = userRepository.findAll(pageable);
        }
        return entityPage.map(userMapper::toDTO);
    }

    @Override
    public Page<UserDTO> findByEmailContaining(String email, Pageable pageable) {
        Page<UserEntity> page = userRepository.findByEmailContaining(email, pageable);
        return new PageImpl<>(page.getContent().stream()
                .map(userMapper::toDTO).toList(), page.getPageable(), page.getTotalElements());
    }

    @Override
    public UserDTO findByEmailAndProviderId(String email, ProviderId providerId) {
        return userRepository.findByEmailAndProviderId(email, providerId)
                .map(userMapper::toDTO)
                .orElse(null);
    }
}
