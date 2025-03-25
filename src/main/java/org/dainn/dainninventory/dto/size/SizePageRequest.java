package org.dainn.dainninventory.dto.size;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.dainn.dainninventory.dto.request.PageRequest;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class SizePageRequest extends PageRequest {
    private String keyword;
    private Integer productId;
    private Integer status = 1;
}
