package org.dainn.dainninventory.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import org.dainn.dainninventory.utils.enums.Provider;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserDTO extends AbstractDTO{
    private String email;
    private String name;

    @JsonIgnore
    private String password;

    private Provider provider;
    private List<String> rolesName = new ArrayList<>();
}
