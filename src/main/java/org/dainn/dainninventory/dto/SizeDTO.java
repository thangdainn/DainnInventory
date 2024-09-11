package org.dainn.dainninventory.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class SizeDTO extends AbstractDTO{
    @NotBlank(message = "Name is required")
    @NotNull(message = "Name is required")
    private String name;
    private String description;

}
