package org.dainn.dainninventory.service.impl;

import lombok.RequiredArgsConstructor;
import org.dainn.dainninventory.controller.request.CategoryPageRequest;
import org.dainn.dainninventory.dto.CategoryDTO;
import org.dainn.dainninventory.entity.CategoryEntity;
import org.dainn.dainninventory.exception.AppException;
import org.dainn.dainninventory.exception.ErrorCode;
import org.dainn.dainninventory.mapper.ICategoryMapper;
import org.dainn.dainninventory.repository.ICategoryRepository;
import org.dainn.dainninventory.service.ICategoryService;
import org.dainn.dainninventory.utils.Paging;
import org.dainn.dainninventory.utils.ValidateString;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor

public class CategoryService implements ICategoryService {
    private final ICategoryRepository categoryRepository;
    private final ICategoryMapper categoryMapper;

    @Transactional
    @Override
    public CategoryDTO save(CategoryDTO dto) {
        CategoryEntity categoryEntity;
        if (dto.getId() != null) {
            CategoryEntity brandOld = categoryRepository.findById(dto.getId())
                    .orElseThrow(() -> new AppException(ErrorCode.CATEGORY_NOT_EXISTED));
            categoryEntity = categoryMapper.updateEntity(brandOld, dto);
        } else {
            categoryRepository.findByName(dto.getName())
                    .ifPresent(role -> {
                        throw new AppException(ErrorCode.CATEGORY_EXISTED);
                    });
            categoryEntity = categoryMapper.toEntity(dto);
        }
        return categoryMapper.toDTO(categoryRepository.save(categoryEntity));
    }

    @Transactional
    @Override
    public void delete(List<Integer> ids) {
        categoryRepository.deleteAllByIdInBatchCustom(ids);
    }

    @Override
    public CategoryDTO findById(Integer id) {
        return categoryMapper.toDTO(categoryRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.CATEGORY_NOT_EXISTED)));
    }

    @Override
    public CategoryDTO findByName(String name) {
        return categoryMapper.toDTO(categoryRepository.findByName(name)
                .orElseThrow(() -> new AppException(ErrorCode.CATEGORY_NOT_EXISTED)));
    }

    @Override
    public List<CategoryDTO> findAll() {
        return categoryRepository.findAll()
                .stream().map(categoryMapper::toDTO).toList();
    }

    @Override
    public List<CategoryDTO> findAll(Integer status) {
        return categoryRepository.findAllByStatus(status)
                .stream().map(categoryMapper::toDTO).toList();
    }

    @Override
    public Page<CategoryDTO> findAllByName(CategoryPageRequest request) {
        return (ValidateString.isNullOrBlank(request.getKeyword())
                ? categoryRepository.findAllByStatus(request.getStatus(), Paging.getPageable(request))
                : categoryRepository.findAllByNameContainingIgnoreCaseAndStatus(request.getKeyword(), request.getStatus(), Paging.getPageable(request))
        ).map(categoryMapper::toDTO);
    }
}
