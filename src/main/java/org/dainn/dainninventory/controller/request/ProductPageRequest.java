package org.dainn.dainninventory.controller.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ProductPageRequest extends PageRequest{
    private String keyword;
    private List<Integer> categoryIds;
    private List<Integer> brandIds;
    private BigDecimal minPrice;
    private BigDecimal maxPrice;
    private Integer status = 1;
}
