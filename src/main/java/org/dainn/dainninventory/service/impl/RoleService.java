package org.dainn.dainninventory.service.impl;

import lombok.RequiredArgsConstructor;
import org.dainn.dainninventory.dto.RoleDTO;
import org.dainn.dainninventory.entity.RoleEntity;
import org.dainn.dainninventory.exception.AppException;
import org.dainn.dainninventory.exception.ErrorCode;
import org.dainn.dainninventory.mapper.IRoleMapper;
import org.dainn.dainninventory.repository.IRoleRepository;
import org.dainn.dainninventory.service.IRoleService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor

public class RoleService implements IRoleService {
    private final IRoleRepository roleRepository;
    private final IRoleMapper roleMapper;

    @Transactional
    @Override
    public RoleDTO save(RoleDTO roleDTO) {
        RoleEntity roleEntity;
        roleDTO.setName("ROLE_" + roleDTO.getName().toUpperCase());
        if (roleDTO.getId() != null) {
            RoleEntity roleOld = roleRepository.findById(roleDTO.getId())
                    .orElseThrow(() -> new AppException(ErrorCode.ROLE_NOT_EXISTED));
            roleEntity = roleMapper.updateEntity(roleOld, roleDTO);
        } else {
            roleRepository.findByName(roleDTO.getName())
                    .ifPresent(role -> {
                        throw new AppException(ErrorCode.ROLE_EXISTED);
                    });
            roleEntity = roleMapper.toEntity(roleDTO);
        }
        return roleMapper.toDTO(roleRepository.save(roleEntity));
    }

    @Transactional
    @Override
    public void delete(List<Integer> ids) {
        roleRepository.deleteAllByIdInBatchCustom(ids);
    }

    @Override
    public RoleDTO findById(Integer id) {
        return roleMapper.toDTO(roleRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.ROLE_NOT_EXISTED)));
    }

    @Override
    public RoleDTO findByName(String name) {
        return roleMapper.toDTO(roleRepository.findByName(name)
                .orElseThrow(() -> new AppException(ErrorCode.ROLE_NOT_EXISTED)));
    }

    @Override
    public List<RoleDTO> findAll() {
        return roleRepository.findAll().stream().map(roleMapper::toDTO).toList();
    }
}
