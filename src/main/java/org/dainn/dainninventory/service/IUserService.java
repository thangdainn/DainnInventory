package org.dainn.dainninventory.service;

import org.dainn.dainninventory.controller.request.UserPageRequest;
import org.dainn.dainninventory.controller.request.UserRequest;
import org.dainn.dainninventory.dto.UserDTO;
import org.dainn.dainninventory.utils.Provider;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface IUserService {
    UserDTO save(UserRequest userRequest);
    void delete(List<Integer> ids);
    UserDTO findById(Integer id);
    List<UserDTO> findAll();
    Page<UserDTO> findAll(Pageable pageable);
    List<UserDTO> findAll(Integer status);
//    UserDTO findByEmailAndProviderId(String email, Provider providerId);

    Page<UserDTO> findWithSpec(UserPageRequest request);
}
