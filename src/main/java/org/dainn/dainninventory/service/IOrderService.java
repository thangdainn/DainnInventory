package org.dainn.dainninventory.service;

import org.dainn.dainninventory.dto.OrderDTO;
import org.dainn.dainninventory.utils.enums.OrderStatus;
import org.springframework.data.domain.Page;

import java.util.List;

public interface IOrderService {
    OrderDTO insert(OrderDTO dto);

    int updateStatus(Integer id, OrderStatus status);
    OrderDTO findById(Integer id);
    List<OrderDTO> findAll();
    Page<OrderDTO> findAll(Integer activeFlag, Integer page, Integer size);
}
