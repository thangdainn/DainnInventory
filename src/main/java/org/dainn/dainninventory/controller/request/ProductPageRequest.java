package org.dainn.dainninventory.controller.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ProductPageRequest extends PageRequest{
    private String keyword;
    private Integer brandId;
    private Integer categoryId;
    private Integer status = 1;
}
