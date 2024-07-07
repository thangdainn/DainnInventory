package org.dainn.dainninventory.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.dainn.dainninventory.controller.request.BrandPageRequest;
import org.dainn.dainninventory.dto.BrandDTO;
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
