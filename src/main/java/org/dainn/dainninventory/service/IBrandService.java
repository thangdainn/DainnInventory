package org.dainn.dainninventory.service;

import org.dainn.dainninventory.dto.brand.BrandPageRequest;
import org.dainn.dainninventory.dto.brand.BrandDTO;
import org.springframework.data.domain.Page;

import java.util.List;

public interface IBrandService {
    BrandDTO insert(BrandDTO dto);
    BrandDTO update(BrandDTO dto);
    void delete(List<Integer> ids);
    BrandDTO findById(Integer id);
    BrandDTO findByName(String name);
    List<BrandDTO> findAll();

    List<BrandDTO> findAll(Integer status);
    Page<BrandDTO> findAllByName(BrandPageRequest request);
}
