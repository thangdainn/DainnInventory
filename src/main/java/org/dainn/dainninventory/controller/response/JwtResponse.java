package org.dainn.dainninventory.controller.response;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class JwtResponse {
    private String token_type = "Bearer";
    private String access_token;

    public JwtResponse(String access_token) {
        this.access_token = access_token;
    }
}
