package org.dainn.dainninventory.dto;

import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class GoodsReceiptDetailDTO {
    private Integer id;

    @NotNull(message = "Price is required")
    private BigDecimal price;

    @NotNull(message = "Quantity is required")
    private Integer quantity;

    @NotNull(message = "Total is required")
    private BigDecimal total;

    @NotNull(message = "Product is required")
    private Integer productId;
}
