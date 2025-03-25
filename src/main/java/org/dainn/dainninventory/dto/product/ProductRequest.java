package org.dainn.dainninventory.dto.product;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.dainn.dainninventory.dto.request.AbstractRequest;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ProductRequest extends AbstractRequest {

    private String description;
    private String image;
    private List<String> imageUrls = new ArrayList<>();

//    @NotNull(message = "Quantity is required")
    private Integer quantity;

    @NotBlank(message = "Name is required")
    @NotNull(message = "Name is required")
    private String name;

    @NotNull(message = "Price is required")
    private BigDecimal price;

    @NotNull(message = "Brand is required")
    private Integer brandId;

    @NotNull(message = "Category is required")
    private Integer categoryId;
}
