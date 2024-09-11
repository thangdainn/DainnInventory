package org.dainn.dainninventory.service.impl;

import com.fasterxml.jackson.core.type.TypeReference;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.dainn.dainninventory.dto.SizeDTO;
import org.dainn.dainninventory.entity.SizeEntity;
import org.dainn.dainninventory.exception.AppException;
import org.dainn.dainninventory.exception.ErrorCode;
import org.dainn.dainninventory.mapper.ISizeMapper;
import org.dainn.dainninventory.repository.ISizeRepository;
import org.dainn.dainninventory.service.IBaseRedisService;
import org.dainn.dainninventory.service.ISizeService;
import org.dainn.dainninventory.utils.constant.RedisConstant;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class SizeService implements ISizeService {
    private final ISizeRepository sizeRepository;
    private final ISizeMapper sizeMapper;
    private final IBaseRedisService baseRedisService;

    @Transactional
    @Override
    public SizeDTO insert(SizeDTO dto) {
        sizeRepository.findByName(dto.getName())
                .ifPresent(brand -> {
                    throw new AppException(ErrorCode.SIZE_EXISTED);
                });
        SizeEntity sizeEntity = sizeMapper.toEntity(dto);
        dto = sizeMapper.toDTO(sizeRepository.save(sizeEntity));
        String key = RedisConstant.SIZE_KEY_PREFIX + "::id:" + dto.getId();
        baseRedisService.setCache(key, dto);
        return dto;
    }

    @Transactional
    @Override
    public SizeDTO update(SizeDTO dto) {
        SizeEntity sizeOld = sizeRepository.findById(dto.getId())
                .orElseThrow(() -> new AppException(ErrorCode.SIZE_NOT_EXISTED));
        SizeEntity sizeEntity = sizeMapper.updateEntity(sizeOld, dto);
        dto = sizeMapper.toDTO(sizeRepository.save(sizeEntity));
        String key = RedisConstant.SIZE_KEY_PREFIX + "::id:" + dto.getId();
        baseRedisService.flushDb();
        baseRedisService.setCache(key, dto);
        return dto;
    }

    @Transactional
    @Override
    public void delete(List<Integer> ids) {
        sizeRepository.deleteAllByIdInBatchCustom(ids);
        baseRedisService.flushDb();
    }

    @Override
    public SizeDTO findById(Integer id) {
        String key = RedisConstant.SIZE_KEY_PREFIX + "::id:" + id;
        SizeDTO sizeDTO = baseRedisService.getCache(key, new TypeReference<SizeDTO>() {});
        if (sizeDTO == null) {
            sizeDTO = sizeMapper.toDTO(sizeRepository.findById(id)
                    .orElseThrow(() -> new AppException(ErrorCode.SIZE_NOT_EXISTED)));
            baseRedisService.setCache(key, sizeDTO);
        }
        return sizeDTO;
    }

    @Override
    public SizeDTO findByName(String name) {
        String key = RedisConstant.SIZE_KEY_PREFIX + "::name:" + name;
        SizeDTO sizeDTO = baseRedisService.getCache(key, new TypeReference<SizeDTO>() {});
        if (sizeDTO == null) {
            sizeDTO = sizeMapper.toDTO(sizeRepository.findByName(name)
                    .orElseThrow(() -> new AppException(ErrorCode.SIZE_NOT_EXISTED)));
            baseRedisService.setCache(key, sizeDTO);
        }
        return sizeDTO;
    }

    @Override
    public List<SizeDTO> findAll() {
        String key = RedisConstant.SIZES_KEY_PREFIX;
        List<SizeDTO> list = baseRedisService.getCache(key, new TypeReference<List<SizeDTO>>() {});
        if (list == null) {
            list = sizeRepository.findAll()
                    .stream().map(sizeMapper::toDTO).toList();
            baseRedisService.setCache(key, list);
        }
        return list;
    }

    @Override
    public List<SizeDTO> findAll(Integer status) {
        String key = RedisConstant.SIZES_KEY_PREFIX + "::status:" + status;
        List<SizeDTO> list = baseRedisService.getCache(key, new TypeReference<List<SizeDTO>>() {});
        if (list == null) {
            list = sizeRepository.findAllByStatus(status)
                    .stream().map(sizeMapper::toDTO).toList();
            baseRedisService.setCache(key, list);
        }
        return list;
    }


//    @Override
//    public Page<SizeDTO> findAllByName(BrandPageRequest request) {
//        String key = RedisConstant.BRANDS_KEY_PREFIX + "::page:" + request.getPage() + "::size:" + request.getSize()
//                + "::sort:" + request.getSortBy() + "::dir:" + request.getSortDir()
//                + "::keyword:" + request.getKeyword() + "::status:" + request.getStatus();
//        Page<SizeDTO> page = baseRedisService.getCache(key, new TypeReference<Page<SizeDTO>>() {});
//        if (page == null) {
//            page = (StringUtils.hasText(request.getKeyword())
//                    ? sizeRepository.findAllByNameContainingIgnoreCaseAndStatus(request.getKeyword(), request.getStatus(), Paging.getPageable(request))
//                    : sizeRepository.findAllByStatus(request.getStatus(), Paging.getPageable(request))
//            ).map(sizeMapper::toDTO);
//            baseRedisService.setCache(key, page);
//        }
//        return page;
//    }
}
