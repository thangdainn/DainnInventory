package org.dainn.dainninventory.service.impl;

import com.fasterxml.jackson.core.type.TypeReference;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.dainn.dainninventory.controller.request.BrandPageRequest;
import org.dainn.dainninventory.dto.BrandDTO;
import org.dainn.dainninventory.entity.BrandEntity;
import org.dainn.dainninventory.exception.AppException;
import org.dainn.dainninventory.exception.ErrorCode;
import org.dainn.dainninventory.mapper.IBrandMapper;
import org.dainn.dainninventory.repository.IBrandRepository;
import org.dainn.dainninventory.service.IBaseRedisService;
import org.dainn.dainninventory.service.IBrandService;
import org.dainn.dainninventory.utils.Paging;
import org.dainn.dainninventory.utils.constant.RedisConstant;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class BrandService implements IBrandService {
    private final IBrandRepository brandRepository;
    private final IBrandMapper brandMapper;
    private final IBaseRedisService baseRedisService;

    @Transactional
    @Override
    public BrandDTO insert(BrandDTO dto) {
        brandRepository.findByName(dto.getName())
                .ifPresent(brand -> {
                    throw new AppException(ErrorCode.BRAND_EXISTED);
                });
        BrandEntity brandEntity = brandMapper.toEntity(dto);
        dto = brandMapper.toDTO(brandRepository.save(brandEntity));
        String key = RedisConstant.BRAND_KEY_PREFIX + "::id:" + dto.getId();
        baseRedisService.setCache(key, dto);
        return dto;
    }

    @Transactional
    @Override
    public BrandDTO update(BrandDTO dto) {
        BrandEntity brandOld = brandRepository.findById(dto.getId())
                .orElseThrow(() -> new AppException(ErrorCode.BRAND_NOT_EXISTED));
        BrandEntity brandEntity = brandMapper.updateEntity(brandOld, dto);
        dto = brandMapper.toDTO(brandRepository.save(brandEntity));
        String key = RedisConstant.BRAND_KEY_PREFIX + "::id:" + dto.getId();
        baseRedisService.flushDb();
        baseRedisService.setCache(key, dto);
        return dto;
    }

    @Transactional
    @Override
    public void delete(List<Integer> ids) {
        brandRepository.deleteAllByIdInBatchCustom(ids);
        baseRedisService.flushDb();
    }

    @Override
    public BrandDTO findById(Integer id) {
        String key = RedisConstant.BRAND_KEY_PREFIX + "::id:" + id;
        BrandDTO brandDTO = baseRedisService.getCache(key, new TypeReference<BrandDTO>() {});
        if (brandDTO == null) {
            brandDTO = brandMapper.toDTO(brandRepository.findById(id)
                    .orElseThrow(() -> new AppException(ErrorCode.BRAND_NOT_EXISTED)));
            baseRedisService.setCache(key, brandDTO);
        }
        return brandDTO;
    }

    @Override
    public BrandDTO findByName(String name) {
        String key = RedisConstant.BRAND_KEY_PREFIX + "::name:" + name;
        BrandDTO brandDTO = baseRedisService.getCache(key, new TypeReference<BrandDTO>() {});
        if (brandDTO == null) {
            brandDTO = brandMapper.toDTO(brandRepository.findByName(name)
                    .orElseThrow(() -> new AppException(ErrorCode.BRAND_NOT_EXISTED)));
            baseRedisService.setCache(key, brandDTO);
        }
        return brandDTO;
    }

    @Override
    public List<BrandDTO> findAll() {
        String key = RedisConstant.BRANDS_KEY_PREFIX;
        List<BrandDTO> list = baseRedisService.getCache(key, new TypeReference<List<BrandDTO>>() {});
        if (list == null) {
            list = brandRepository.findAll()
                    .stream().map(brandMapper::toDTO).toList();
            baseRedisService.setCache(key, list);
        }
        return list;
    }

    @Override
    public List<BrandDTO> findAll(Integer status) {
        String key = RedisConstant.BRANDS_KEY_PREFIX + "::status:" + status;
        List<BrandDTO> list = baseRedisService.getCache(key, new TypeReference<List<BrandDTO>>() {});
        if (list == null) {
            list = brandRepository.findAllByStatus(status)
                    .stream().map(brandMapper::toDTO).toList();
            baseRedisService.setCache(key, list);
        }
        return list;
    }

    @Override
    public Page<BrandDTO> findAllByName(BrandPageRequest request) {
        String key = RedisConstant.BRANDS_KEY_PREFIX + "::page:" + request.getPage() + "::size:" + request.getSize()
                + "::sort:" + request.getSortBy() + "::dir:" + request.getSortDir()
                + "::keyword:" + request.getKeyword() + "::status:" + request.getStatus();
        Page<BrandDTO> page = baseRedisService.getCache(key, new TypeReference<Page<BrandDTO>>() {});
        if (page == null) {
            page = (StringUtils.hasText(request.getKeyword())
                    ? brandRepository.findAllByNameContainingIgnoreCaseAndStatus(request.getKeyword(), request.getStatus(), Paging.getPageable(request))
                    : brandRepository.findAllByStatus(request.getStatus(), Paging.getPageable(request))
            ).map(brandMapper::toDTO);
            baseRedisService.setCache(key, page);
        }
        return page;
    }
}
