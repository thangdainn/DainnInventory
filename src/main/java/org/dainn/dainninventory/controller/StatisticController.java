package org.dainn.dainninventory.controller;

import lombok.RequiredArgsConstructor;
import org.dainn.dainninventory.config.endpoint.Endpoint;
import org.dainn.dainninventory.dto.statistic.AnalyticDTO;
import org.dainn.dainninventory.service.IProductService;
import org.dainn.dainninventory.service.IStatisticService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(Endpoint.Analytics.BASE)
@RequiredArgsConstructor
public class StatisticController {
    private final IStatisticService statisticService;
    private final IProductService productService;

    @GetMapping(Endpoint.Analytics.STATS)
    public ResponseEntity<?> getStats(@ModelAttribute AnalyticDTO request) {
        return ResponseEntity.ok(statisticService.getStatistic(request.getStartDate(), request.getEndDate()));
    }

    @GetMapping(Endpoint.Analytics.REVENUE)
    public ResponseEntity<?> getRevenueData(@ModelAttribute AnalyticDTO request) {
        return ResponseEntity.ok(statisticService.getRevenueData(request.getStartDate(), request.getEndDate()));
    }

    @GetMapping(Endpoint.Analytics.SALES_BY_CATE)
    public ResponseEntity<?> getSalesByCategory(@ModelAttribute AnalyticDTO request) {
        return ResponseEntity.ok(statisticService.getSalesByCategory(request.getStartDate(), request.getEndDate()));
    }

    @GetMapping(Endpoint.Analytics.TOP_PRODUCT)
    public ResponseEntity<?> getTopProducts(@ModelAttribute AnalyticDTO request) {
        return ResponseEntity.ok(statisticService.getTopProducts(request.getStartDate(), request.getEndDate()));
    }

    @GetMapping(Endpoint.Analytics.RECENT_SALES)
    public ResponseEntity<?> getRecentSales(@ModelAttribute AnalyticDTO request) {
        return ResponseEntity.ok(productService.findRecentSale(request.getStartDate(), request.getEndDate()));
    }
}
