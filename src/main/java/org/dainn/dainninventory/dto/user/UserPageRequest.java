package org.dainn.dainninventory.dto.user;

import lombok.*;
import org.dainn.dainninventory.dto.request.PageRequest;
import org.dainn.dainninventory.utils.enums.Provider;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserPageRequest extends PageRequest {
    private String keyword;
    private List<Integer> roleIds;
    private List<Provider> providers;
    private Integer status = 1;
}
