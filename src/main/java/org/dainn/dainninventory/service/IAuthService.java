package org.dainn.dainninventory.service;

import jakarta.servlet.http.HttpServletResponse;
import org.dainn.dainninventory.controller.request.LoginRequest;
import org.dainn.dainninventory.controller.request.Oauth2Request;
import org.dainn.dainninventory.controller.request.RegisterRequest;
import org.dainn.dainninventory.controller.response.JwtResponse;
import org.dainn.dainninventory.dto.UserDTO;
import org.dainn.dainninventory.utils.enums.Provider;
import org.springframework.web.bind.annotation.RequestHeader;

public interface IAuthService {
    JwtResponse login(LoginRequest request, HttpServletResponse response);
    UserDTO register(RegisterRequest request);
    JwtResponse loginOauth2(String authorizationHeader, Oauth2Request oauth2Request, Provider provider, HttpServletResponse response);
}
