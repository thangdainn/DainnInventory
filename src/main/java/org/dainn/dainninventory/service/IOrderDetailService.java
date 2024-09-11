package org.dainn.dainninventory.service;

import org.dainn.dainninventory.dto.OrderDetailDTO;
import org.dainn.dainninventory.entity.OrderEntity;

import java.util.List;

public interface IOrderDetailService {
    void insert(List<OrderDetailDTO> list, OrderEntity order);
    List<OrderDetailDTO> findByOrderId(Integer orderId);
    void deleteByOrderId(Integer orderId);
}
