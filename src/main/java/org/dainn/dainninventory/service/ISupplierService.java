package org.dainn.dainninventory.service;

import org.dainn.dainninventory.dto.BrandDTO;
import org.dainn.dainninventory.dto.SupplierDTO;

import java.util.List;

public interface ISupplierService {
    SupplierDTO save(SupplierDTO dto);
    void delete(List<Integer> ids);
    SupplierDTO findById(Integer id);
    SupplierDTO findByName(String name);
    List<SupplierDTO> findAll();
}
