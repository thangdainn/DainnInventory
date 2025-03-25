package org.dainn.dainninventory.service.impl;

import lombok.RequiredArgsConstructor;
import org.dainn.dainninventory.dto.category.CategoryDTO;
import org.dainn.dainninventory.dto.product.ProductStats;
import org.dainn.dainninventory.dto.statistic.DailyRevenueDTO;
import org.dainn.dainninventory.dto.statistic.SaleByCateDTO;
import org.dainn.dainninventory.dto.statistic.StatisticDTO;
import org.dainn.dainninventory.entity.OrderDetailEntity;
import org.dainn.dainninventory.entity.OrderEntity;
import org.dainn.dainninventory.repository.IOrderDetailRepository;
import org.dainn.dainninventory.repository.IOrderRepository;
import org.dainn.dainninventory.repository.IProductRepository;
import org.dainn.dainninventory.service.ICategoryService;
import org.dainn.dainninventory.service.IStatisticService;
import org.dainn.dainninventory.utils.enums.OrderStatus;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@Service
@RequiredArgsConstructor
public class StatisticService implements IStatisticService {
    private final IProductRepository productRepository;
    private final IOrderDetailRepository orderDetailRepository;
    private final IOrderRepository orderRepository;
    private final ICategoryService categoryService;

    @Override
    public StatisticDTO getStatistic(Date startDate, Date endDate) {
        List<OrderDetailEntity> orderDetails = orderDetailRepository.findAllByOrder_OrderDateBetween(startDate, endDate);
        List<OrderEntity> orders = orderRepository.findAllByOrderDateBetween(startDate, endDate);
        return StatisticDTO.builder()
                .productSold(orderDetails.stream().map(OrderDetailEntity::getQuantity).reduce(0, Integer::sum))
                .totalRevenue(orders.stream().map(OrderEntity::getTotalAmount).reduce(BigDecimal.ZERO, BigDecimal::add))
                .newOrders(orders.size())
                .build();
    }

    @Override
    public List<DailyRevenueDTO> getRevenueData(Date startDate, Date endDate) {
        Sort sort = Sort.by(Sort.Order.asc("orderDate"));
        List<OrderEntity> orders = orderRepository.findAllByStatusAndOrderDateBetween(OrderStatus.COMPLETED, startDate, endDate, sort);
        return orders.stream().map(
                        order -> DailyRevenueDTO.builder()
                                .date(order.getOrderDate())
                                .revenue(order.getTotalAmount())
                                .build())
                .toList();
    }

    @Override
    public List<SaleByCateDTO> getSalesByCategory(Date startDate, Date endDate) {
        List<CategoryDTO> categories = categoryService.findAll(1);
        List<SaleByCateDTO> salesByCategory = orderRepository.getSalesByCategory(startDate, endDate, OrderStatus.COMPLETED);
        for (CategoryDTO category : categories) {
            boolean isExist = false;
            for (SaleByCateDTO sale : salesByCategory) {
                if (sale.getCategory().equals(category.getName())) {
                    isExist = true;
                    break;
                }
            }
            if (!isExist) {
                salesByCategory.add(new SaleByCateDTO(category.getName(), 0L, BigDecimal.ZERO));
            }
        }
        return salesByCategory;
    }

    @Override
    public List<ProductStats> getTopProducts(Date startDate, Date endDate) {
        return productRepository.getTopProducts(startDate, endDate, OrderStatus.COMPLETED);
    }
}
