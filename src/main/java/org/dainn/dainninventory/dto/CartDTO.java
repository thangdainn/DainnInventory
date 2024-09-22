package org.dainn.dainninventory.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CartDTO {
    private Integer id;

    @NotNull(message = "Quantity is required")
    private Integer quantity;

    @NotNull(message = "Product ID is required")
    private Integer productId;

    @NotNull(message = "Size ID is required")
    private Integer sizeId;

    @NotNull(message = "User ID is required")
    private Integer userId;

    private ProductDTO product;
    private SizeDTO size;
}
