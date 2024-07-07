package org.dainn.dainninventory.service.impl;

import com.fasterxml.jackson.core.type.TypeReference;
import lombok.RequiredArgsConstructor;
import org.dainn.dainninventory.controller.request.SupplierPageRequest;
import org.dainn.dainninventory.dto.SupplierDTO;
import org.dainn.dainninventory.entity.SupplierEntity;
import org.dainn.dainninventory.exception.AppException;
import org.dainn.dainninventory.exception.ErrorCode;
import org.dainn.dainninventory.mapper.ISupplierMapper;
import org.dainn.dainninventory.repository.ISupplierRepository;
import org.dainn.dainninventory.service.IBaseRedisService;
import org.dainn.dainninventory.service.ISupplierService;
import org.dainn.dainninventory.utils.Paging;
import org.dainn.dainninventory.utils.constant.RedisConstant;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SupplierService implements ISupplierService {
    private final ISupplierRepository supplierRepository;
    private final ISupplierMapper supplierMapper;
    private final IBaseRedisService baseRedisService;

    @Transactional
    @Override
    public SupplierDTO insert(SupplierDTO dto) {
        supplierRepository.findByName(dto.getName())
                .ifPresent(role -> {
                    throw new AppException(ErrorCode.SUPPLIER_EXISTED);
                });
        SupplierEntity entity = supplierMapper.toEntity(dto);
        dto = supplierMapper.toDTO(supplierRepository.save(entity));
        String key = RedisConstant.SUPPLIER_KEY_PREFIX + "::id:" + dto.getId();
        baseRedisService.setCache(key, dto);
        return dto;
    }

    @Transactional
    @Override
    public SupplierDTO update(SupplierDTO dto) {
        SupplierEntity old = supplierRepository.findById(dto.getId())
                .orElseThrow(() -> new AppException(ErrorCode.SUPPLIER_NOT_EXISTED));
        SupplierEntity entity = supplierMapper.updateEntity(old, dto);
        dto = supplierMapper.toDTO(supplierRepository.save(entity));
        String key = RedisConstant.SUPPLIER_KEY_PREFIX + "::id:" + dto.getId();
        baseRedisService.flushDb();
        baseRedisService.setCache(key, dto);
        return dto;
    }

    @Transactional
    @Override
    public void delete(List<Integer> ids) {
        supplierRepository.deleteAllByIdInBatchCustom(ids);
        baseRedisService.flushDb();
    }

    @Override
    public SupplierDTO findById(Integer id) {
        String key = RedisConstant.SUPPLIER_KEY_PREFIX + "::id:" + id;
        SupplierDTO dto = baseRedisService.getCache(key, new TypeReference<SupplierDTO>() {});
        if (dto == null){
            dto = supplierMapper.toDTO(supplierRepository.findById(id)
                    .orElseThrow(() -> new AppException(ErrorCode.SUPPLIER_NOT_EXISTED)));
            baseRedisService.setCache(key, dto);
        }
        return dto;
    }

    @Override
    public SupplierDTO findByName(String name) {
        String key = RedisConstant.SUPPLIER_KEY_PREFIX + "::name:" + name;
        SupplierDTO dto = baseRedisService.getCache(key, new TypeReference<SupplierDTO>() {});
        if (dto == null){
            dto = supplierMapper.toDTO(supplierRepository.findByName(name)
                    .orElseThrow(() -> new AppException(ErrorCode.SUPPLIER_NOT_EXISTED)));
        }
        return dto;
    }

    @Override
    public List<SupplierDTO> findAll() {
        String key = RedisConstant.SUPPLIERS_KEY_PREFIX;
        List<SupplierDTO> list = baseRedisService.getCache(key, new TypeReference<List<SupplierDTO>>() {});
        if (list == null){
            list = supplierRepository.findAll()
                    .stream().map(supplierMapper::toDTO).toList();
            baseRedisService.setCache(key, list);
        }
        return list;
    }

    @Override
    public List<SupplierDTO> findAll(Integer status) {
        String key = RedisConstant.SUPPLIERS_KEY_PREFIX + "::status:" + status;
        List<SupplierDTO> list = baseRedisService.getCache(key, new TypeReference<List<SupplierDTO>>() {});
        if (list == null) {
            list = supplierRepository.findAllByStatus(status)
                    .stream().map(supplierMapper::toDTO).toList();
            baseRedisService.setCache(key, list);
        }
        return list;
    }

    @Override
    public Page<SupplierDTO> findAllByName(SupplierPageRequest request) {
        String key = RedisConstant.SUPPLIERS_KEY_PREFIX + "::page:" + request.getPage() + "::size:" + request.getSize()
                + "::sort:" + request.getSortBy() + "::dir:" + request.getSortDir()
                + "::keyword:" + request.getKeyword() + "::status:" + request.getStatus();
        Page<SupplierDTO> page = baseRedisService.getCache(key, new TypeReference<Page<SupplierDTO>>() {});
        if (page == null) {
            page = (StringUtils.hasText(request.getKeyword())
                    ? supplierRepository.findAllByNameContainingIgnoreCaseAndStatus(request.getKeyword(), request.getStatus(), Paging.getPageable(request))
                    : supplierRepository.findAllByStatus(request.getStatus(), Paging.getPageable(request))
            ).map(supplierMapper::toDTO);
            baseRedisService.setCache(key, page);
        }
        return page;
    }
}
