package org.dainn.dainninventory.service;

import org.dainn.dainninventory.dto.GoodsReceiptDTO;
import org.dainn.dainninventory.dto.OrderDTO;
import org.dainn.dainninventory.utils.OrderStatus;

import java.util.List;

public interface IOrderService {
    OrderDTO insert(OrderDTO dto);

    int updateStatus(Integer id, OrderStatus status);
    OrderDTO findById(Integer id);
    List<OrderDTO> findAll();
}
