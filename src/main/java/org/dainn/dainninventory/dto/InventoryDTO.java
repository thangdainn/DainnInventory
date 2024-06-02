package org.dainn.dainninventory.dto;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class InventoriesDTO extends AbstractDTO{
    private Integer quantity;
    private Integer reorderPoint;
    private Integer productId;
}
