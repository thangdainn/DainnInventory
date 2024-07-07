package org.dainn.dainninventory.service;

import org.dainn.dainninventory.controller.request.SupplierPageRequest;
import org.dainn.dainninventory.dto.SupplierDTO;
import org.springframework.data.domain.Page;

import java.util.List;

public interface ISupplierService {
    SupplierDTO insert(SupplierDTO dto);
    SupplierDTO update(SupplierDTO dto);
    void delete(List<Integer> ids);
    SupplierDTO findById(Integer id);
    SupplierDTO findByName(String name);
    List<SupplierDTO> findAll();
    List<SupplierDTO> findAll(Integer status);
    Page<SupplierDTO> findAllByName(SupplierPageRequest request);
}
