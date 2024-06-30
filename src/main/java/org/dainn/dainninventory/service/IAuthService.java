package org.dainn.dainninventory.service;

import jakarta.servlet.http.HttpServletResponse;
import org.dainn.dainninventory.controller.request.LoginRequest;
import org.dainn.dainninventory.controller.response.JwtResponse;

public interface IAuthService {
    JwtResponse login(LoginRequest request, HttpServletResponse response);
    void logout();
    void register(String username, String password);
}
