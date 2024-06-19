package org.dainn.dainninventory.dto;

import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class InventoryDTO extends AbstractDTO{
    @NotNull(message = "Quantity is required")
    private Integer quantity;
    private Integer reorderPoint = 0;
    private Integer productId;
}
