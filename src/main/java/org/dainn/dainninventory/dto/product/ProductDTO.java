package org.dainn.dainninventory.dto.product;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.dainn.dainninventory.dto.AbstractDTO;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ProductDTO extends AbstractDTO {
    @NotBlank(message = "Code is required")
    @NotNull(message = "Code is required")
    private String code;

    private String description;
    private String image;

    @NotBlank(message = "Name is required")
    @NotNull(message = "Name is required")
    private String name;

    @NotNull(message = "Price is required")
    private BigDecimal price;

    @NotNull(message = "Brand is required")
    private Integer brandId;

    private String brandName;

    @NotNull(message = "Category is required")
    private Integer categoryId;

    private String categoryName;

    private Integer stock;

    private List<String> imageUrls = new ArrayList<>();
}
