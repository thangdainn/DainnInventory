package org.dainn.dainninventory.service;

import org.dainn.dainninventory.dto.size.SizePageRequest;
import org.dainn.dainninventory.dto.size.SizeDTO;
import org.springframework.data.domain.Page;

import java.util.List;

public interface ISizeService {
    SizeDTO insert(SizeDTO dto);
    SizeDTO update(SizeDTO dto);
    void delete(List<Integer> ids);
    SizeDTO findById(Integer id);
    SizeDTO findByName(String name);
    List<SizeDTO> findAll();

    List<SizeDTO> findAll(Integer status);
    Page<SizeDTO> findAllByName(SizePageRequest request);
}
