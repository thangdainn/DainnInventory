package org.dainn.dainninventory.dto;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ProductDTO extends AbstractDTO{
    private String code;
    private String description;
    private String imgUrl;
    private String name;
    private double price;
    private Integer brandId;
    private Integer categoryId;
}
