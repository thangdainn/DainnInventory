package org.dainn.dainninventory.service.impl;

import lombok.RequiredArgsConstructor;
import org.dainn.dainninventory.controller.request.CategoryPageRequest;
import org.dainn.dainninventory.controller.request.SupplierPageRequest;
import org.dainn.dainninventory.dto.CategoryDTO;
import org.dainn.dainninventory.dto.SupplierDTO;
import org.dainn.dainninventory.entity.SupplierEntity;
import org.dainn.dainninventory.exception.AppException;
import org.dainn.dainninventory.exception.ErrorCode;
import org.dainn.dainninventory.mapper.ISupplierMapper;
import org.dainn.dainninventory.repository.ISupplierRepository;
import org.dainn.dainninventory.service.ISupplierService;
import org.dainn.dainninventory.utils.Paging;
import org.dainn.dainninventory.utils.ValidateString;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor

public class SupplierService implements ISupplierService {
    private final ISupplierRepository supplierRepository;
    private final ISupplierMapper supplierMapper;

    @Transactional
    @Override
    public SupplierDTO save(SupplierDTO dto) {
        SupplierEntity brandEntity;
        if (dto.getId() != null) {
            SupplierEntity old = supplierRepository.findById(dto.getId())
                    .orElseThrow(() -> new AppException(ErrorCode.SUPPLIER_NOT_EXISTED));
            brandEntity = supplierMapper.updateEntity(old, dto);
        } else {
            supplierRepository.findByName(dto.getName())
                    .ifPresent(role -> {
                        throw new AppException(ErrorCode.SUPPLIER_EXISTED);
                    });
            brandEntity = supplierMapper.toEntity(dto);
        }
        return supplierMapper.toDTO(supplierRepository.save(brandEntity));
    }

    @Transactional
    @Override
    public void delete(List<Integer> ids) {
        supplierRepository.deleteAllByIdInBatchCustom(ids);
    }

    @Override
    public SupplierDTO findById(Integer id) {
        return supplierMapper.toDTO(supplierRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.SUPPLIER_NOT_EXISTED)));
    }

    @Override
    public SupplierDTO findByName(String name) {
        return supplierMapper.toDTO(supplierRepository.findByName(name)
                .orElseThrow(() -> new AppException(ErrorCode.SUPPLIER_NOT_EXISTED)));
    }

    @Override
    public List<SupplierDTO> findAll() {
        return supplierRepository.findAll()
                .stream().map(supplierMapper::toDTO).toList();
    }

    @Override
    public List<SupplierDTO> findAll(Integer status) {
        return supplierRepository.findAllByStatus(status)
                .stream().map(supplierMapper::toDTO).toList();
    }

    @Override
    public Page<SupplierDTO> findAllByName(SupplierPageRequest request) {
        return (ValidateString.isNullOrBlank(request.getKeyword())
                ? supplierRepository.findAllByStatus(request.getStatus(), Paging.getPageable(request))
                : supplierRepository.findAllByNameContainingIgnoreCaseAndStatus(request.getKeyword(), request.getStatus(), Paging.getPageable(request))
        ).map(supplierMapper::toDTO);
    }
}
