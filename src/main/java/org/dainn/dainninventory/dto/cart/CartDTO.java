package org.dainn.dainninventory.dto.cart;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.dainn.dainninventory.dto.AbstractDTO;
import org.dainn.dainninventory.dto.size.SizeDTO;
import org.dainn.dainninventory.dto.product.ProductDTO;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CartDTO extends AbstractDTO {

    @NotNull(message = "Quantity is required")
    private Integer quantity;

    @NotNull(message = "Product ID is required")
    private Integer productId;

    @NotNull(message = "Size ID is required")
    private Integer sizeId;

    @NotNull(message = "User ID is required")
    private Integer userId;

    private Integer stock;

    private ProductDTO product;
    private SizeDTO size;
}
