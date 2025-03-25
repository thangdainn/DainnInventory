package org.dainn.dainninventory.service;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.dainn.dainninventory.dto.auth.LoginDTO;
import org.dainn.dainninventory.dto.auth.RegisterDTO;
import org.dainn.dainninventory.dto.response.JwtResponse;
import org.dainn.dainninventory.dto.device.DeviceInfoDTO;
import org.dainn.dainninventory.dto.user.UserDTO;
import org.dainn.dainninventory.dto.auth.ResetPassword;

public interface IAuthService {
    JwtResponse login(LoginDTO request, HttpServletResponse response);
    UserDTO register(RegisterDTO request);
    JwtResponse loginGoogle(HttpServletRequest request, DeviceInfoDTO deviceInfo, HttpServletResponse response);
    void forgotPassword(ResetPassword dto);
}
