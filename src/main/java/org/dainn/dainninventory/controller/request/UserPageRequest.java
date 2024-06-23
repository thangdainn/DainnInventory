package org.dainn.dainninventory.controller.request;

import lombok.*;
import org.dainn.dainninventory.utils.enums.Provider;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserPageRequest extends PageRequest {
    private String keyword;
    private Integer roleId;
    private Provider provider;
    private Integer status = 1;
}
