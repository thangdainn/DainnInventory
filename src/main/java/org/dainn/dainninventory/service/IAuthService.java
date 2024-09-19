package org.dainn.dainninventory.service;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.dainn.dainninventory.controller.request.LoginRequest;
import org.dainn.dainninventory.controller.request.RegisterRequest;
import org.dainn.dainninventory.controller.response.JwtResponse;
import org.dainn.dainninventory.dto.DeviceInfoDTO;
import org.dainn.dainninventory.dto.UserDTO;

public interface IAuthService {
    JwtResponse login(LoginRequest request, HttpServletResponse response);
    UserDTO register(RegisterRequest request);
    JwtResponse loginGoogle(HttpServletRequest request, DeviceInfoDTO deviceInfo, HttpServletResponse response);
}
