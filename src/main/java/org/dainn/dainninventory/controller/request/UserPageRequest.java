package org.dainn.dainninventory.controller.request;

import lombok.*;
import org.dainn.dainninventory.entity.RoleEntity;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserRequest extends PageRequest{
    private String keyword;
}
