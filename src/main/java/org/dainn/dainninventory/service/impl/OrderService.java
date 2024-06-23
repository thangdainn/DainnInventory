package org.dainn.dainninventory.service.impl;

import lombok.RequiredArgsConstructor;
import org.dainn.dainninventory.dto.OrderDTO;
import org.dainn.dainninventory.entity.OrderEntity;
import org.dainn.dainninventory.exception.AppException;
import org.dainn.dainninventory.exception.ErrorCode;
import org.dainn.dainninventory.mapper.IOrderMapper;
import org.dainn.dainninventory.repository.IOrderRepository;
import org.dainn.dainninventory.repository.IUserRepository;
import org.dainn.dainninventory.service.IOrderDetailService;
import org.dainn.dainninventory.service.IOrderService;
import org.dainn.dainninventory.utils.enums.OrderStatus;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderService implements IOrderService {
    private final IUserRepository userRepository;
    private final IOrderRepository orderRepository;
    private final IOrderDetailService orderDetailService;
    private final IOrderMapper orderMapper;

    @Transactional
    @Override
    public OrderDTO insert(OrderDTO dto) {
        OrderEntity entity = orderMapper.toEntity(dto);
        entity.setUser(userRepository.findById(dto.getUserId())
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED)));
        entity = orderRepository.save(entity);
        orderDetailService.insert(dto.getDetailDTOS(), entity.getId());
        return orderMapper.toDTO(entity);
    }

    @Transactional
    @Override
    public int updateStatus(Integer id, OrderStatus status) {
        return orderRepository.updateStatus(id, status);
    }


    @Override
    public OrderDTO findById(Integer id) {
        OrderDTO dto = orderMapper.toDTO(orderRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.ORDER_NOT_EXISTED)));
        dto.setDetailDTOS(orderDetailService.findByOrderId(id));
        return dto;
    }


    @Override
    public List<OrderDTO> findAll() {
        return orderRepository.findAll()
                .stream().map(orderMapper::toDTO).toList();
    }

    @Override
    public Page<OrderDTO> findAll(Integer activeFlag, Integer page, Integer size) {
        return null;
    }
}
