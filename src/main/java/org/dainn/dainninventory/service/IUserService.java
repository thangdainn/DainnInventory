package org.dainn.dainninventory.service;

import org.dainn.dainninventory.dto.RoleDTO;

import java.util.List;

public interface IRoleService {
    RoleDTO save(RoleDTO roleDTO);
    void delete(List<Integer> ids);
    RoleDTO findById(Integer id);
    RoleDTO findByName(String name);
    List<RoleDTO> findAll();
}
