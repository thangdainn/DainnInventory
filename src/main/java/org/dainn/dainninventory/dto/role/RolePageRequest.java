package org.dainn.dainninventory.dto.role;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.dainn.dainninventory.dto.request.PageRequest;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class RolePageRequest extends PageRequest {
    private String keyword;
    private Integer status = 1;
}
