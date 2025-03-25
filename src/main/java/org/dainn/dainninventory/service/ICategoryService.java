package org.dainn.dainninventory.service;

import org.dainn.dainninventory.dto.category.CategoryPageRequest;
import org.dainn.dainninventory.dto.category.CategoryDTO;
import org.springframework.data.domain.Page;

import java.util.List;

public interface ICategoryService {
    CategoryDTO insert(CategoryDTO dto);
    CategoryDTO update(CategoryDTO dto);
    void delete(List<Integer> ids);
    CategoryDTO findById(Integer id);
    CategoryDTO findByName(String name);
    List<CategoryDTO> findAll();
    List<CategoryDTO> findAll(Integer status);
    Page<CategoryDTO> findAllByName(CategoryPageRequest request);
}
