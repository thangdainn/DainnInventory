package org.dainn.dainninventory.service;

import jakarta.servlet.http.HttpServletRequest;
import org.dainn.dainninventory.dto.user.UserPageRequest;
import org.dainn.dainninventory.dto.user.UserRequest;
import org.dainn.dainninventory.dto.user.UserDTO;
import org.dainn.dainninventory.dto.user.UpdateProfile;
import org.dainn.dainninventory.utils.enums.Provider;
import org.springframework.data.domain.Page;

import java.util.List;

public interface IUserService {
    UserDTO insert(UserRequest userRequest);
    UserDTO update(UserRequest userRequest);
    void delete(List<Integer> ids);
    boolean checkEmailAndProvider(String email, Provider provider);
    UserDTO findById(Integer id);
    UserDTO findMyInfo(HttpServletRequest request);
    UserDTO updateProfile(UpdateProfile dto, HttpServletRequest request);
    UserDTO findByEmailAndProvider(String email, Provider provider);
    List<UserDTO> findAll();
    List<UserDTO> findAll(Integer status);
    Page<UserDTO> findWithSpec(UserPageRequest request);
}
