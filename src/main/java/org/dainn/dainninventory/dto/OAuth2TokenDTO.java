package org.dainn.dainninventory.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class OAuth2TokenDTO {
    private String token;
    private String deviceInfo;
}
