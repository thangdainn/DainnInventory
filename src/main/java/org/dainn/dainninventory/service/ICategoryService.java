package org.dainn.dainninventory.service;

import org.dainn.dainninventory.controller.request.BrandPageRequest;
import org.dainn.dainninventory.controller.request.CategoryPageRequest;
import org.dainn.dainninventory.dto.BrandDTO;
import org.dainn.dainninventory.dto.CategoryDTO;
import org.dainn.dainninventory.entity.CategoryEntity;
import org.springframework.data.domain.Page;

import java.util.List;

public interface ICategoryService {
    CategoryDTO save(CategoryDTO dto);
    void delete(List<Integer> ids);
    CategoryDTO findById(Integer id);
    CategoryDTO findByName(String name);
    List<CategoryDTO> findAll();
    List<CategoryDTO> findAll(Integer status);
    Page<CategoryDTO> findAllByName(CategoryPageRequest request);
}
