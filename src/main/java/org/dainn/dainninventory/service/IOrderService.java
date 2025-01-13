package org.dainn.dainninventory.service;

import org.dainn.dainninventory.controller.request.MyOrderPageRequest;
import org.dainn.dainninventory.controller.request.OrderPageRequest;
import org.dainn.dainninventory.dto.Order.MyOrderDTO;
import org.dainn.dainninventory.dto.OrderDTO;
import org.dainn.dainninventory.utils.enums.OrderStatus;
import org.springframework.data.domain.Page;

import java.util.List;

public interface IOrderService {
    OrderDTO insert(OrderDTO dto);
    void updateStatus(Integer id, OrderStatus status);
    void updateIsPaid(Integer id);
    OrderDTO findById(Integer id);
    List<OrderDTO> findAll();
    Page<OrderDTO> findWithSpec(OrderPageRequest request);
    Page<MyOrderDTO> findMyOrderWithSpec(MyOrderPageRequest request);

}
