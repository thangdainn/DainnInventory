package org.dainn.dainninventory.dto.brand;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.dainn.dainninventory.dto.AbstractDTO;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class BrandDTO extends AbstractDTO {
    @NotBlank(message = "Name is required")
    @NotNull(message = "Name is required")
    private String name;
    private String description;

}
