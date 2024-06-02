package org.dainn.dainninventory.dto;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UsersDTO extends AbstractDTO{
    private String email;
    private String name;
    private String password;
    private String providerId;
}
