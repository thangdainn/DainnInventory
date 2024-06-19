package org.dainn.dainninventory.controller.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.dainn.dainninventory.utils.ProviderId;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserRequest {

    private Integer id;
    @NotBlank(message = "Email is required")
    @Email(message = "Email is invalid")
    private String email;
    @NotBlank(message = "Name is required")

    private String name;
    private ProviderId providerId = ProviderId.local;
    private String password;
    private Integer status;
    private LocalDateTime createdDate;
    private LocalDateTime modifiedDate;
    private List<String> rolesName = new ArrayList<>();
}
