package org.dainn.dainninventory.dto.statistic;

import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class StatisticDTO {
    private BigDecimal totalRevenue;
    private Integer newOrders;
    private Integer productSold;
}
