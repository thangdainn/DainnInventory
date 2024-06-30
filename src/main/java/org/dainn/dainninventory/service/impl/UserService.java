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
import org.dainn.dainninventory.repository.specification.SearchOperation;
import org.dainn.dainninventory.repository.specification.SpecSearchCriteria;
import org.dainn.dainninventory.repository.specification.SpecificationBuilder;
import org.dainn.dainninventory.service.IUserService;
import org.dainn.dainninventory.utils.Paging;
import org.dainn.dainninventory.utils.constant.RoleConstant;
import org.dainn.dainninventory.utils.enums.Provider;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
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

    @Transactional
    @Override
    public UserDTO save(UserRequest userRequest) {
        UserDTO userDTO = userMapper.toDTO(userRequest);
        UserEntity userEntity;
        if (userDTO.getId() != null) {
            UserEntity userOld = userRepository.findById(userDTO.getId())
                    .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));
            if (!userOld.getEmail().equals(userDTO.getEmail()) && checkEmailAndProvider(userDTO.getEmail(), userDTO.getProvider())) {
                throw new AppException(ErrorCode.EMAIL_EXISTED);
            }
            if (userDTO.getPassword().isBlank()) {
                userDTO.setPassword(userOld.getPassword());
            } else {
                userDTO.setPassword(encoder.encode(userDTO.getPassword()));
            }
            userEntity = userMapper.updateEntity(userOld, userDTO);
        } else {
            if (checkEmailAndProvider(userDTO.getEmail(), userDTO.getProvider())) {
                throw new AppException(ErrorCode.EMAIL_EXISTED);
            }
            userEntity = userMapper.toEntity(userDTO);
            userEntity.setPassword(encoder.encode(userDTO.getPassword()));
        }
        List<RoleEntity> roles = new ArrayList<>();
        if (userDTO.getRolesName().isEmpty()) {
            roles.add(roleRepository.findByName(RoleConstant.PREFIX_ROLE + "USER")
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

    private boolean checkEmailAndProvider(String email, Provider provider) {
        return userRepository.existsByEmailAndProvider(email, provider);
    }

    @Transactional
    @Override
    public void delete(List<Integer> ids) {
        userRepository.deleteAllByIdInBatchCustom(ids);
    }

    @Override
    public UserDTO findById(Integer id) {
        return userRepository.findById(id)
                .map(userMapper::toDTO)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));
    }

    @Override
    public UserDTO findByEmailAndProvider(String email, Provider provider) {
        return userRepository.findByEmailAndProvider(email, provider)
                .map(userMapper::toDTO)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));
    }


    @Override
    public List<UserDTO> findAll() {
        return userRepository.findAll()
                .stream().map(userMapper::toDTO).toList();
    }

    @Override
    public Page<UserDTO> findAll(Pageable pageable) {
        return userRepository.findAll(pageable)
                .map(userMapper::toDTO);
    }

    @Override
    public List<UserDTO> findAll(Integer status) {
        return userRepository.findAllByStatus(status)
                .stream().map(userMapper::toDTO).toList();
    }

    @Override
    public Page<UserDTO> findWithSpec(UserPageRequest request) {
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
        return page.map(userMapper::toDTO);

    }

}
