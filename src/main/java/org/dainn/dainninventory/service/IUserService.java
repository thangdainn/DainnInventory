package org.dainn.dainninventory.service;

import org.dainn.dainninventory.controller.request.UserPageRequest;
import org.dainn.dainninventory.controller.response.PageResponse;
import org.dainn.dainninventory.controller.response.UserResponse;
import org.dainn.dainninventory.dto.UserDTO;
import org.dainn.dainninventory.utils.ProviderId;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface IUserService {
    UserResponse save(UserDTO userDTO);
    void delete(List<Integer> ids);
    UserResponse findById(Integer id);
    UserDTO findByEmail(String email);
    List<UserResponse> findAll();
    Page<UserResponse> findAll(Pageable pageable);
    Page<UserResponse> findByEmailContaining(String email, Pageable pageable);

    UserDTO findByEmailAndProviderId(String userName, ProviderId providerId);
}
