package org.dainn.dainninventory.dto.user;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.*;
import org.dainn.dainninventory.dto.request.AbstractRequest;
import org.dainn.dainninventory.utils.enums.Provider;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserRequest extends AbstractRequest {

    @NotBlank(message = "Email is required")
    @Email(message = "Email is invalid")
    private String email;

    @NotBlank(message = "Name is required")
    private String name;
    private String avatar;
    private String phone;
    private Provider provider = Provider.local;
    private String password;
    private String roleName;
}
