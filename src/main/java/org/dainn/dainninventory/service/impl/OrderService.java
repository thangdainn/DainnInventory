package org.dainn.dainninventory.service.impl;

import lombok.RequiredArgsConstructor;
import org.dainn.dainninventory.controller.request.OrderPageRequest;
import org.dainn.dainninventory.dto.OrderDTO;
import org.dainn.dainninventory.entity.OrderEntity;
import org.dainn.dainninventory.exception.AppException;
import org.dainn.dainninventory.exception.ErrorCode;
import org.dainn.dainninventory.mapper.IOrderMapper;
import org.dainn.dainninventory.repository.IOrderRepository;
import org.dainn.dainninventory.repository.IUserRepository;
import org.dainn.dainninventory.repository.specification.SearchOperation;
import org.dainn.dainninventory.repository.specification.SpecSearchCriteria;
import org.dainn.dainninventory.repository.specification.SpecificationBuilder;
import org.dainn.dainninventory.service.IOrderDetailService;
import org.dainn.dainninventory.service.IOrderService;
import org.dainn.dainninventory.utils.Paging;
import org.dainn.dainninventory.utils.ValidateString;
import org.dainn.dainninventory.utils.enums.OrderStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

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
    public Page<OrderDTO> findWithSpec(OrderPageRequest request) {
        SpecificationBuilder<OrderEntity> builder = new SpecificationBuilder<>();
        Page<OrderEntity> page;
        Specification<OrderEntity> spec = null;
        if (!ValidateString.isNullOrBlank(request.getKeyword())){
            builder.with("customerName", SearchOperation.CONTAINS, request.getKeyword(), true);
            builder.with("customerPhone", SearchOperation.CONTAINS, request.getKeyword(), true);
            try {
                builder.with("id", SearchOperation.EQUALITY, Integer.valueOf(request.getKeyword()), true);
            } catch (NumberFormatException e){
                // do nothing
            }
        }
        if (request.getStatus() != null){
            builder.with("status", SearchOperation.EQUALITY, request.getStatus(), false);
        }
        if (request.getFromDate() != null){
            builder.with("orderDate", SearchOperation.GREATER_THAN_OR_EQUAL, request.getFromDate(), false);
        }
        if (request.getToDate() != null){
            builder.with("orderDate", SearchOperation.LESS_THAN_OR_EQUAL, request.getToDate(), false);
        }
        spec = builder.build();
        if (request.getUserId() != null){
            List<SpecSearchCriteria> userCriteria = new ArrayList<>();
            userCriteria.add(new SpecSearchCriteria("id", SearchOperation.EQUALITY, request.getUserId(), true));
            Specification<OrderEntity> userSpec = builder.joinTableWithCondition("user", userCriteria);
            spec = Specification.where(spec).and(userSpec);

        }
        if (spec == null){
            page = orderRepository.findAll(Paging.getPageable(request));
            return page.map(orderMapper::toDTO);
        }
        page = orderRepository.findAll(Objects.requireNonNull(spec), Paging.getPageable(request));
        return page.map(orderMapper::toDTO);
    }
}
