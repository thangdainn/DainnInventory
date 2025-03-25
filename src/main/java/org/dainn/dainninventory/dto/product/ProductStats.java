package org.dainn.dainninventory.dto.product;

import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ProductStats {
    private Integer id;
    private String name;
    private String image;
    private Long sold;
    private BigDecimal revenue;
}
