package org.dainn.dainninventory.service;

import org.dainn.dainninventory.controller.request.RolePageRequest;
import org.dainn.dainninventory.dto.RoleDTO;
import org.springframework.data.domain.Page;

import java.util.List;

public interface IRoleService {
    RoleDTO save(RoleDTO roleDTO);
    void delete(List<Integer> ids);
    RoleDTO findById(Integer id);
    RoleDTO findByName(String name);
    List<RoleDTO> findAll();

    List<RoleDTO> findAll(Integer status);

    Page<RoleDTO> findAllByName(RolePageRequest request);
}
