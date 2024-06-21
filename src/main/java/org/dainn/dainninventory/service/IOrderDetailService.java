package org.dainn.dainninventory.service;

import org.dainn.dainninventory.dto.GoodsReceiptDetailDTO;
import org.dainn.dainninventory.dto.OrderDetailDTO;

import java.util.List;

public interface IOrderDetailService {
    void insert(List<OrderDetailDTO> list, Integer orderId);
    List<OrderDetailDTO> findByOrderId(Integer orderId);
    void deleteByOrderId(Integer orderId);
}
