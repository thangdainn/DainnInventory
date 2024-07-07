package org.dainn.dainninventory.service.impl;

import com.fasterxml.jackson.core.type.TypeReference;
import lombok.RequiredArgsConstructor;
import org.dainn.dainninventory.controller.request.CategoryPageRequest;
import org.dainn.dainninventory.dto.CategoryDTO;
import org.dainn.dainninventory.entity.CategoryEntity;
import org.dainn.dainninventory.exception.AppException;
import org.dainn.dainninventory.exception.ErrorCode;
import org.dainn.dainninventory.mapper.ICategoryMapper;
import org.dainn.dainninventory.repository.ICategoryRepository;
import org.dainn.dainninventory.service.IBaseRedisService;
import org.dainn.dainninventory.service.ICategoryService;
import org.dainn.dainninventory.utils.Paging;
import org.dainn.dainninventory.utils.constant.RedisConstant;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.List;

@Service
@RequiredArgsConstructor

public class CategoryService implements ICategoryService {
    private final ICategoryRepository categoryRepository;
    private final ICategoryMapper categoryMapper;
    private final IBaseRedisService baseRedisService;

    @Transactional
    @Override
    public CategoryDTO insert(CategoryDTO dto) {
        categoryRepository.findByName(dto.getName())
                .ifPresent(role -> {
                    throw new AppException(ErrorCode.CATEGORY_EXISTED);
                });
        CategoryEntity categoryEntity = categoryMapper.toEntity(dto);
        dto = categoryMapper.toDTO(categoryRepository.save(categoryEntity));
        String key = RedisConstant.CATEGORY_KEY_PREFIX + "::id:" + dto.getId();
        baseRedisService.setCache(key, dto);
        return dto;
    }

    @Transactional
    @Override
    public CategoryDTO update(CategoryDTO dto) {
        CategoryEntity brandOld = categoryRepository.findById(dto.getId())
                .orElseThrow(() -> new AppException(ErrorCode.CATEGORY_NOT_EXISTED));
        CategoryEntity categoryEntity = categoryMapper.updateEntity(brandOld, dto);
        dto = categoryMapper.toDTO(categoryRepository.save(categoryEntity));

        String key = RedisConstant.CATEGORY_KEY_PREFIX + "::id:" + dto.getId();
        baseRedisService.flushDb();
        baseRedisService.setCache(key, dto);
        return dto;
    }

    @Transactional
    @Override
    public void delete(List<Integer> ids) {
        categoryRepository.deleteAllByIdInBatchCustom(ids);
        baseRedisService.flushDb();
    }

    @Override
    public CategoryDTO findById(Integer id) {
        String key = RedisConstant.CATEGORY_KEY_PREFIX + "::id:" + id;
        CategoryDTO dto = baseRedisService.getCache(key, new TypeReference<CategoryDTO>() {});
        if (dto == null){
            dto = categoryMapper.toDTO(categoryRepository.findById(id)
                    .orElseThrow(() -> new AppException(ErrorCode.CATEGORY_NOT_EXISTED)));
            baseRedisService.setCache(key, dto);
        }
        return dto;
    }

    @Override
    public CategoryDTO findByName(String name) {
        String key = RedisConstant.CATEGORY_KEY_PREFIX + "::name:" + name;
        CategoryDTO dto = baseRedisService.getCache(key, new TypeReference<CategoryDTO>() {});
        if (dto == null){
            dto = categoryMapper.toDTO(categoryRepository.findByName(name)
                    .orElseThrow(() -> new AppException(ErrorCode.CATEGORY_NOT_EXISTED)));
            baseRedisService.setCache(key, dto);
        }
        return dto;
    }

    @Override
    public List<CategoryDTO> findAll() {
        String key = RedisConstant.CATEGORIES_KEY_PREFIX;
        List<CategoryDTO> list = baseRedisService.getCache(key, new TypeReference<List<CategoryDTO>>() {});
        if (list == null){
            list = categoryRepository.findAll()
                    .stream().map(categoryMapper::toDTO).toList();
            baseRedisService.setCache(key, list);
        }
        return list;
    }

    @Override
    public List<CategoryDTO> findAll(Integer status) {
        String key = RedisConstant.CATEGORIES_KEY_PREFIX + "::status:" + status;
        List<CategoryDTO> list = baseRedisService.getCache(key, new TypeReference<List<CategoryDTO>>() {});
        if (list == null) {
            list = categoryRepository.findAllByStatus(status)
                    .stream().map(categoryMapper::toDTO).toList();
            baseRedisService.setCache(key, list);
        }
        return list;
    }

    @Override
    public Page<CategoryDTO> findAllByName(CategoryPageRequest request) {
        String key = RedisConstant.CATEGORIES_KEY_PREFIX + "::page:" + request.getPage() + "::size:" + request.getSize()
                + "::sort:" + request.getSortBy() + "::dir:" + request.getSortDir()
                + "::keyword:" + request.getKeyword() + "::status:" + request.getStatus();
        Page<CategoryDTO> page = baseRedisService.getCache(key, new TypeReference<Page<CategoryDTO>>() {});
        if (page == null) {
            page = (StringUtils.hasText(request.getKeyword())
                    ? categoryRepository.findAllByNameContainingIgnoreCaseAndStatus(request.getKeyword(), request.getStatus(), Paging.getPageable(request))
                    : categoryRepository.findAllByStatus(request.getStatus(), Paging.getPageable(request))
            ).map(categoryMapper::toDTO);
            baseRedisService.setCache(key, page);
        }
        return page;
    }
}
