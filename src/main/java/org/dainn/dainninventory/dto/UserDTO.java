package org.dainn.dainninventory.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.*;
import org.dainn.dainninventory.utils.ProviderId;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserDTO extends AbstractDTO{
    @NotBlank(message = "Email is required")
    @Email(message = "Email is invalid")
    private String email;

    @NotBlank(message = "Name is required")
    private String name;
    private String password;
    private ProviderId providerId = ProviderId.local;
    private List<String> rolesName = new ArrayList<>();
}
