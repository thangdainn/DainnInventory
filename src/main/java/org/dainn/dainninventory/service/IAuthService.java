package org.dainn.dainninventory.service;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.dainn.dainninventory.dto.auth.LoginDTO;
import org.dainn.dainninventory.dto.auth.RegisterDTO;
import org.dainn.dainninventory.dto.auth.ResetPassword;
import org.dainn.dainninventory.dto.response.JwtResponse;
import org.dainn.dainninventory.dto.user.UserDTO;

public interface IAuthService {
    JwtResponse login(LoginDTO dto, HttpServletRequest request, HttpServletResponse response);
    UserDTO register(RegisterDTO request);
    JwtResponse loginGoogle(HttpServletRequest request, HttpServletResponse response);
    void forgotPassword(ResetPassword dto);
    boolean checkPassword(ResetPassword dto);
    void resetPassword(ResetPassword dto, HttpServletRequest request);
}
