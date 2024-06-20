package org.dainn.dainninventory.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class SupplierDTO extends AbstractDTO {
    @NotBlank(message = "Address is required")
    @NotNull(message = "Address is required")
    private String address;

    @NotBlank(message = "Name is required")
    @NotNull(message = "Name is required")
    private String name;

    @NotBlank(message = "Phone is required")
    @NotNull(message = "Phone is required")
    private String phone;
}
