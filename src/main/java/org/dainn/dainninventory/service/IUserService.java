package org.dainn.dainninventory.service;

import org.dainn.dainninventory.controller.request.UserPageRequest;
import org.dainn.dainninventory.controller.request.UserRequest;
import org.dainn.dainninventory.dto.UserDTO;
import org.dainn.dainninventory.utils.ProviderId;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface IUserService {
    UserDTO save(UserRequest userRequest);
    void delete(List<Integer> ids);
    UserDTO findById(Integer id);
    UserDTO findByEmail(String email);
    List<UserDTO> findAll();
    Page<UserDTO> findAll(Pageable pageable);
    Page<UserDTO> findAll(UserPageRequest request);
    Page<UserDTO> findByEmailContaining(String email, Pageable pageable);

    UserDTO findByEmailAndProviderId(String userName, ProviderId providerId);
}
