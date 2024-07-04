package org.dainn.dainninventory.controller.request;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Oauth2Request {
    @NotBlank(message = "Device info is required")
    private String deviceInfo;
}
