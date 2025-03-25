package org.dainn.dainninventory.service;

import org.dainn.dainninventory.dto.product.ProductStats;
import org.dainn.dainninventory.dto.statistic.DailyRevenueDTO;
import org.dainn.dainninventory.dto.statistic.SaleByCateDTO;
import org.dainn.dainninventory.dto.statistic.StatisticDTO;

import java.util.Date;
import java.util.List;

public interface IStatisticService {
    StatisticDTO getStatistic(Date startDate, Date endDate);
    List<DailyRevenueDTO> getRevenueData(Date startDate, Date endDate);
    List<SaleByCateDTO> getSalesByCategory(Date startDate, Date endDate);
    List<ProductStats> getTopProducts(Date startDate, Date endDate);
}
