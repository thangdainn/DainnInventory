package org.dainn.dainninventory.dto.statistic;

import lombok.*;

import java.math.BigDecimal;
import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DailyRevenueDTO {
    private Date date;
    private BigDecimal revenue;
}
