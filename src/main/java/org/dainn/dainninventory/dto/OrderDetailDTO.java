package org.dainn.dainninventory.dto;

import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class OrderDetailDTO extends AbstractDTO{
    @NotNull(message = "Price is required")
    private double price;

    @NotNull(message = "Quantity is required")
    private Integer quantity;

    @NotNull(message = "Total is required")
    private double total;

    @NotNull(message = "Product is required")
    private Integer productId;
}
