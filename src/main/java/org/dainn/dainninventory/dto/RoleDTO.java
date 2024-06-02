package org.dainn.dainninventory.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class RolesDTO extends AbstractDTO<RolesDTO>{
    private String description;
    private String name;
}
