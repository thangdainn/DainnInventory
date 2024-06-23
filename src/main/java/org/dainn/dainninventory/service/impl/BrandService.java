package org.dainn.dainninventory.service.impl;

import lombok.RequiredArgsConstructor;
import org.dainn.dainninventory.controller.request.BrandPageRequest;
import org.dainn.dainninventory.dto.BrandDTO;
import org.dainn.dainninventory.entity.BrandEntity;
import org.dainn.dainninventory.exception.AppException;
import org.dainn.dainninventory.exception.ErrorCode;
import org.dainn.dainninventory.mapper.IBrandMapper;
import org.dainn.dainninventory.repository.IBrandRepository;
import org.dainn.dainninventory.service.IBrandService;
import org.dainn.dainninventory.utils.Paging;
import org.dainn.dainninventory.utils.ValidateString;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor

public class BrandService implements IBrandService {
    private final IBrandRepository brandRepository;
    private final IBrandMapper brandMapper;

    @Transactional
    @Override
    public BrandDTO save(BrandDTO dto) {
        BrandEntity brandEntity;
        if (dto.getId() != null) {
            BrandEntity brandOld = brandRepository.findById(dto.getId())
                    .orElseThrow(() -> new AppException(ErrorCode.BRAND_NOT_EXISTED));
            brandEntity = brandMapper.updateEntity(brandOld, dto);
        } else {
            brandRepository.findByName(dto.getName())
                    .ifPresent(role -> {
                        throw new AppException(ErrorCode.BRAND_EXISTED);
                    });
            brandEntity = brandMapper.toEntity(dto);
        }
        return brandMapper.toDTO(brandRepository.save(brandEntity));
    }

    @Transactional
    @Override
    public void delete(List<Integer> ids) {
        brandRepository.deleteAllByIdInBatchCustom(ids);
    }

    @Override
    public BrandDTO findById(Integer id) {
        return brandMapper.toDTO(brandRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.BRAND_NOT_EXISTED)));
    }

    @Override
    public BrandDTO findByName(String name) {
        return brandMapper.toDTO(brandRepository.findByName(name)
                .orElseThrow(() -> new AppException(ErrorCode.BRAND_NOT_EXISTED)));
    }

    @Override
    public List<BrandDTO> findAll() {
        return brandRepository.findAll()
                .stream().map(brandMapper::toDTO).toList();
    }

    @Override
    public List<BrandDTO> findAll(Integer status) {
        return brandRepository.findAllByStatus(status)
                .stream().map(brandMapper::toDTO).toList();
    }

    @Override
    public Page<BrandDTO> findAllByName(BrandPageRequest request) {
        return (ValidateString.isNullOrBlank(request.getKeyword())
                ? brandRepository.findAllByStatus(request.getStatus(), Paging.getPageable(request))
                : brandRepository.findAllByNameContainingIgnoreCaseAndStatus(request.getKeyword(), request.getStatus(), Paging.getPageable(request))
        ).map(brandMapper::toDTO);
    }
}
