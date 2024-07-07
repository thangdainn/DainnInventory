package org.dainn.dainninventory.service.impl;

import com.fasterxml.jackson.core.type.TypeReference;
import lombok.RequiredArgsConstructor;
import org.dainn.dainninventory.controller.request.RolePageRequest;
import org.dainn.dainninventory.dto.RoleDTO;
import org.dainn.dainninventory.entity.RoleEntity;
import org.dainn.dainninventory.exception.AppException;
import org.dainn.dainninventory.exception.ErrorCode;
import org.dainn.dainninventory.mapper.IRoleMapper;
import org.dainn.dainninventory.repository.IRoleRepository;
import org.dainn.dainninventory.service.IBaseRedisService;
import org.dainn.dainninventory.service.IRoleService;
import org.dainn.dainninventory.utils.Paging;
import org.dainn.dainninventory.utils.constant.RedisConstant;
import org.dainn.dainninventory.utils.constant.RoleConstant;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.List;

@Service
@RequiredArgsConstructor

public class RoleService implements IRoleService {
    private final IRoleRepository roleRepository;
    private final IRoleMapper roleMapper;
    private final IBaseRedisService baseRedisService;

    @Transactional
    @Override
    public RoleDTO insert(RoleDTO dto) {
        dto.setName(RoleConstant.PREFIX_ROLE + dto.getName().toUpperCase());
        roleRepository.findByName(dto.getName())
                .ifPresent(role -> {
                    throw new AppException(ErrorCode.ROLE_EXISTED);
                });
        RoleEntity entity = roleMapper.toEntity(dto);
        dto = roleMapper.toDTO(roleRepository.save(entity));
        String key = RedisConstant.ROLE_KEY_PREFIX + "::id:" + dto.getId();
        baseRedisService.setCache(key, dto);
        return dto;
    }

    @Transactional
    @Override
    public RoleDTO update(RoleDTO dto) {
        dto.setName(RoleConstant.PREFIX_ROLE + dto.getName().toUpperCase());
        RoleEntity roleOld = roleRepository.findById(dto.getId())
                .orElseThrow(() -> new AppException(ErrorCode.ROLE_NOT_EXISTED));
        RoleEntity entity = roleMapper.updateEntity(roleOld, dto);
        dto = roleMapper.toDTO(roleRepository.save(entity));
        String key = RedisConstant.ROLE_KEY_PREFIX + "::id:" + dto.getId();
        baseRedisService.flushDb();
        baseRedisService.setCache(key, dto);
        return dto;
    }

    @Transactional
    @Override
    public void delete(List<Integer> ids) {
        roleRepository.deleteAllByIdInBatchCustom(ids);
        baseRedisService.flushDb();
    }

    @Override
    public RoleDTO findById(Integer id) {
        String key = RedisConstant.ROLE_KEY_PREFIX + "::id:" + id;
        RoleDTO roleDTO = baseRedisService.getCache(key, new TypeReference<RoleDTO>() {});
        if (roleDTO == null){
            roleDTO = roleMapper.toDTO(roleRepository.findById(id)
                    .orElseThrow(() -> new AppException(ErrorCode.ROLE_NOT_EXISTED)));
            baseRedisService.setCache(key, roleDTO);
        }
        return roleDTO;
    }

    @Override
    public RoleDTO findByName(String name) {
        String key = RedisConstant.ROLE_KEY_PREFIX + "::name:" + name;
        RoleDTO roleDTO = baseRedisService.getCache(key, new TypeReference<RoleDTO>() {});
        if (roleDTO == null){
            roleDTO = roleMapper.toDTO(roleRepository.findByName(name)
                    .orElseThrow(() -> new AppException(ErrorCode.ROLE_NOT_EXISTED)));
            baseRedisService.setCache(key, roleDTO);
        }
        return roleDTO;
    }

    @Override
    public List<RoleDTO> findAll() {
        String key = RedisConstant.ROLES_KEY_PREFIX;
        List<RoleDTO> list = baseRedisService.getCache(key, new TypeReference<List<RoleDTO>>() {});
        if (list == null){
            list = roleRepository.findAll()
                    .stream().map(roleMapper::toDTO).toList();
            baseRedisService.setCache(key, list);
        }
        return list;
    }

    @Override
    public List<RoleDTO> findAll(Integer status) {
        String key = RedisConstant.ROLES_KEY_PREFIX + "::status:" + status;
        List<RoleDTO> list = baseRedisService.getCache(key, new TypeReference<List<RoleDTO>>() {});
        if (list == null) {
            list = roleRepository.findAllByStatus(status)
                    .stream().map(roleMapper::toDTO).toList();
            baseRedisService.setCache(key, list);
        }
        return list;
    }

    @Override
    public Page<RoleDTO> findAllByName(RolePageRequest request) {
        String key = RedisConstant.ROLES_KEY_PREFIX + "::page:" + request.getPage() + "::size:" + request.getSize()
                + "::sort:" + request.getSortBy() + "::dir:" + request.getSortDir()
                + "::keyword:" + request.getKeyword() + "::status:" + request.getStatus();
        Page<RoleDTO> page = baseRedisService.getCache(key, new TypeReference<Page<RoleDTO>>() {});
        if (page == null) {
            page = (StringUtils.hasText(request.getKeyword())
                    ? roleRepository.findAllByNameContainingIgnoreCaseAndStatus(request.getKeyword(), request.getStatus(), Paging.getPageable(request))
                    : roleRepository.findAllByStatus(request.getStatus(), Paging.getPageable(request))
            ).map(roleMapper::toDTO);
            baseRedisService.setCache(key, page);
        }
        return page;
    }
}
