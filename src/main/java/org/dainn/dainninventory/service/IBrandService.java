package org.dainn.dainninventory.service;

import org.dainn.dainninventory.dto.BrandDTO;
import org.dainn.dainninventory.dto.RoleDTO;
import org.dainn.dainninventory.entity.BrandEntity;

import java.util.List;

public interface IBrandService {
    BrandDTO save(BrandDTO dto);
    void delete(List<Integer> ids);
    BrandDTO findById(Integer id);
    BrandDTO findByName(String name);
    List<BrandDTO> findAll();
}
