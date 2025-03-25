package org.dainn.dainninventory.service;

import org.dainn.dainninventory.dto.order.MyOrderPageRequest;
import org.dainn.dainninventory.dto.order.OrderPageRequest;
import org.dainn.dainninventory.dto.order.OrderDTO;
import org.dainn.dainninventory.dto.order.MyOrderDTO;
import org.dainn.dainninventory.utils.enums.OrderStatus;
import org.springframework.data.domain.Page;

import java.util.Date;
import java.util.List;

public interface IOrderService {
    OrderDTO insert(OrderDTO dto);
    void updateStatuses(List<Integer> ids, OrderStatus status);
    void updateIsPaid(Integer id);
    OrderDTO findById(Integer id);
    List<OrderDTO> findAll();
    Page<OrderDTO> findWithSpec(OrderPageRequest request);
    Page<MyOrderDTO> findMyOrderWithSpec(MyOrderPageRequest request);
    List<OrderDTO> findByProductId(Integer id, Date startDate, Date endDate);
}
