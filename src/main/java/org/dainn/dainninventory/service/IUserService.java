package org.dainn.dainninventory.service;

import org.dainn.dainninventory.controller.request.UserPageRequest;
import org.dainn.dainninventory.controller.request.UserRequest;
import org.dainn.dainninventory.dto.UserDTO;
import org.dainn.dainninventory.utils.enums.Provider;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface IUserService {
    UserDTO insert(UserRequest userRequest);
    UserDTO update(UserRequest userRequest);
    void delete(List<Integer> ids);
    UserDTO findById(Integer id);
    UserDTO findByEmailAndProvider(String email, Provider provider);
    List<UserDTO> findAll();
    List<UserDTO> findAll(Integer status);
    Page<UserDTO> findWithSpec(UserPageRequest request);
}
