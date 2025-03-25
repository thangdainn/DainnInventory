package org.dainn.dainninventory.dto.statistic;

import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SaleByCateDTO {
    private String category;
    private Long quantity;
    private BigDecimal revenue;
}
