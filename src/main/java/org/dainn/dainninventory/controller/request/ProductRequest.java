package org.dainn.dainninventory.controller.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.dainn.dainninventory.dto.AbstractDTO;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ProductRequest extends AbstractRequest{
    @NotBlank(message = "Code is required")
    @NotNull(message = "Code is required")
    private String code;

    private String description;
    private String imgUrl;

    @NotNull(message = "Quantity is required")
    private Integer quantity;

    private Integer reorderPoint = 0;

    @NotBlank(message = "Name is required")
    @NotNull(message = "Name is required")
    private String name;

    @NotNull(message = "Price is required")
    private double price;

    @NotNull(message = "Brand is required")
    private Integer brandId;

    @NotNull(message = "Category is required")
    private Integer categoryId;
}
