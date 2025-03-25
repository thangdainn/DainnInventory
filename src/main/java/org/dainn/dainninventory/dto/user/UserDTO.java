package org.dainn.dainninventory.dto.user;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.dainn.dainninventory.dto.AbstractDTO;
import org.dainn.dainninventory.utils.enums.Provider;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
//@Builder
public class UserDTO extends AbstractDTO {
    private String email;
    private String name;
    private String avatar;
    private String phone;
    @JsonIgnore
    private String password;

    private Provider provider;
    private String roleName;
}
