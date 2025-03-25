package org.dainn.dainninventory.service.impl;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.dainn.dainninventory.dto.user.UpdateProfile;
import org.dainn.dainninventory.dto.user.UserDTO;
import org.dainn.dainninventory.dto.user.UserPageRequest;
import org.dainn.dainninventory.dto.user.UserRequest;
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
import org.dainn.dainninventory.service.IUserService;
import org.dainn.dainninventory.utils.JwtUtil;
import org.dainn.dainninventory.utils.Paging;
import org.dainn.dainninventory.utils.constant.RoleConstant;
import org.dainn.dainninventory.utils.enums.Provider;
import org.springframework.data.domain.Page;
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
        userEntity.setRole(handleRole(userDTO.getRoleName()));
        return userMapper.toDTO(userRepository.save(userEntity));
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
        userEntity.setRole(handleRole(userDTO.getRoleName()));
        return userMapper.toDTO(userRepository.save(userEntity));
    }

    @Override
    public boolean checkEmailAndProvider(String email, Provider provider) {
        return userRepository.existsByEmailAndProvider(email, provider);
    }

    private RoleEntity handleRole(String roleName) {
        if (roleName == null || roleName.isBlank()) {
            return roleRepository.findByName(RoleConstant.PREFIX_ROLE + "USER")
                    .orElseThrow(() -> new AppException(ErrorCode.ROLE_NOT_EXISTED));
        }
        return roleRepository.findByName(RoleConstant.PREFIX_ROLE + roleName)
                .orElseThrow(() -> new AppException(ErrorCode.ROLE_NOT_EXISTED));
    }

    @Transactional
    @Override
    public void delete(List<Integer> ids) {
        userRepository.deleteAllByIdInBatchCustom(ids);
    }

    @Override
    public UserDTO findById(Integer id) {
        return userMapper.toDTO(userRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED)));
    }

    @Override
    public UserDTO findMyInfo(HttpServletRequest request) {
        String jwt = JwtUtil.getJwtFromRequest(request);
        Integer userId = jwtProvider.extractId(jwt);
        return userMapper.toDTO(userRepository.findById(userId)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED)));
    }

    @Override
    public UserDTO updateProfile(UpdateProfile dto, HttpServletRequest request) {
        String jwt = JwtUtil.getJwtFromRequest(request);
        Integer userId = jwtProvider.extractId(jwt);
        UserEntity user = userRepository.findById(userId)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));
        user.setPhone(dto.getPhone());
        user.setName(dto.getName());
        user.setAvatar(dto.getAvatar());
        return userMapper.toDTO(userRepository.save(user));
    }

    @Override
    public UserDTO findByEmailAndProvider(String email, Provider provider) {
        return userMapper.toDTO(userRepository.findByEmailAndProviderAndStatus(email, provider, 1)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED)));
    }


    @Override
    public List<UserDTO> findAll() {
        return userRepository.findAll()
                .stream().map(userMapper::toDTO).toList();
    }


    @Override
    public List<UserDTO> findAll(Integer status) {
            return userRepository.findAllByStatus(status)
                    .stream().map(userMapper::toDTO).toList();
    }

    @Override
    public Page<UserDTO> findWithSpec(UserPageRequest request) {
        SpecificationBuilder<UserEntity> builder = new SpecificationBuilder<>();
        Specification<UserEntity> spec;
        if (StringUtils.hasText(request.getKeyword())) {
            builder.with("email", SearchOperation.CONTAINS, request.getKeyword(), true);
            builder.with("name", SearchOperation.CONTAINS, request.getKeyword(), true);
        }
        if (isNonNullOrNonEmpty(request.getProviders())) {
            for (Provider provider : request.getProviders()) {
                builder.with("provider", SearchOperation.EQUALITY, provider, true);
            }
        }
        builder.with("status", SearchOperation.EQUALITY, request.getStatus(), false);
        spec = builder.build();
        if (isNonNullOrNonEmpty(request.getRoleIds())) {
            List<SpecSearchCriteria> roleCriteria = new ArrayList<>();
            for (Integer roleId : request.getRoleIds()) {
                roleCriteria.add(new SpecSearchCriteria("id", SearchOperation.EQUALITY, roleId, true));
            }
            Specification<UserEntity> roleSpec = builder.joinTableWithCondition("role", roleCriteria);
            spec = Specification.where(spec).and(roleSpec);
        }
        Page<UserEntity> page = userRepository.findAll(Objects.requireNonNull(spec), Paging.getPageable(request));
        return page.map(userMapper::toDTO);
    }

    private <T> boolean isNonNullOrNonEmpty(List<T> list) {
        return list != null && !list.isEmpty();
    }
}
