package org.dainn.dainninventory.dto;

import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ProductSizeDTO {
    private Integer id;

    @NotNull(message = "Quantity is required")
    private Integer quantity;

    @NotNull(message = "Product is required")
    private Integer productId;

    @NotNull(message = "Size is required")
    private Integer sizeId;

    private String sizeName;
}
