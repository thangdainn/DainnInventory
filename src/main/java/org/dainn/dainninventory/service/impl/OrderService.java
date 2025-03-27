package org.dainn.dainninventory.service.impl;

import jakarta.persistence.criteria.Join;
import lombok.RequiredArgsConstructor;
import org.dainn.dainninventory.dto.order.MyOrderPageRequest;
import org.dainn.dainninventory.dto.order.OrderPageRequest;
import org.dainn.dainninventory.dto.order.MyOrderDTO;
import org.dainn.dainninventory.dto.order.OrderDTO;
import org.dainn.dainninventory.entity.OrderDetailEntity;
import org.dainn.dainninventory.entity.OrderEntity;
import org.dainn.dainninventory.entity.ProductEntity;
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
import org.dainn.dainninventory.utils.enums.OrderStatus;
import org.dainn.dainninventory.utils.enums.PaymentMethod;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.Date;
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
        if (entity.getPaymentMethod() != PaymentMethod.Cash) {
            entity.setStatus(OrderStatus.TO_PAY);
        }
        entity.setUser(userRepository.findById(dto.getUserId())
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED)));
        entity = orderRepository.save(entity);
        orderDetailService.insert(dto.getDetails(), entity);
        return orderMapper.toDTO(entity);
    }

    @Transactional
    @Override
    public void updateStatuses(List<Integer> ids, OrderStatus status) {
        for (Integer id : ids) {
            OrderEntity order = orderRepository.findById(id)
                    .orElseThrow(() -> new AppException(ErrorCode.ORDER_NOT_EXISTED));
            switch (status) {
                case CANCELLED:
                    if (order.getStatus() != OrderStatus.PROCESSING && order.getStatus() != OrderStatus.TO_PAY) {
                        throw new AppException(ErrorCode.ORDER_STATUS_INVALID);
                    }
                    break;
                case COMPLETED:
                    if (order.getStatus() != OrderStatus.SHIPPING) {
                        throw new AppException(ErrorCode.ORDER_STATUS_INVALID);
                    }
                    break;
                case SHIPPING:
                    if (order.getStatus() != OrderStatus.PROCESSING) {
                        throw new AppException(ErrorCode.ORDER_STATUS_INVALID);
                    }
                    break;
                default:
                    throw new AppException(ErrorCode.ORDER_STATUS_INVALID);
            }
        }
        orderRepository.updateStatuses(ids, status);
    }

    @Transactional
    @Override
    public void updateIsPaid(Integer id) {
        orderRepository.updatePaid(id, true, OrderStatus.PROCESSING);
    }

    @Override
    public OrderDTO findById(Integer id) {
        OrderDTO orderDto = orderMapper.toDTO(orderRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.ORDER_NOT_EXISTED)));
        orderDto.setDetails(orderDetailService.findByOrderId(id));
        return orderDto;
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
        Specification<OrderEntity> spec;
        if (StringUtils.hasText(request.getKeyword())) {
            builder.with("customerName", SearchOperation.CONTAINS, request.getKeyword(), true);
            builder.with("customerPhone", SearchOperation.CONTAINS, request.getKeyword(), true);
            try {
                builder.with("id", SearchOperation.EQUALITY, Integer.valueOf(request.getKeyword()), true);
            } catch (NumberFormatException e) {
                // do nothing
            }
        }
        if (request.getStatus() != null) {
            builder.with("status", SearchOperation.EQUALITY, request.getStatus(), false);
        }
        if (request.getFromDate() != null) {
            builder.with("orderDate", SearchOperation.GREATER_THAN_OR_EQUAL, request.getFromDate(), false);
        }
        if (request.getToDate() != null) {
            builder.with("orderDate", SearchOperation.LESS_THAN_OR_EQUAL, request.getToDate(), false);
        }
        spec = builder.build();
        if (request.getUserId() != null) {
            List<SpecSearchCriteria> userCriteria = new ArrayList<>();
            userCriteria.add(new SpecSearchCriteria("id", SearchOperation.EQUALITY, request.getUserId(), true));
            Specification<OrderEntity> userSpec = builder.joinTableWithCondition("user", userCriteria);
            spec = Specification.where(spec).and(userSpec);

        }
        if (spec == null) {
            page = orderRepository.findAll(Paging.getPageable(request));
            return page.map(orderMapper::toDTO);
        }
        page = orderRepository.findAll(Objects.requireNonNull(spec), Paging.getPageable(request));
        return page.map(orderMapper::toDTO);
    }

    @Override
    public Page<MyOrderDTO> findMyOrderWithSpec(MyOrderPageRequest request) {
        SpecificationBuilder<OrderEntity> builder = new SpecificationBuilder<>();
        Specification<OrderEntity> spec;
        if (StringUtils.hasText(request.getKeyword())) {
            try {
                builder.with("id", SearchOperation.EQUALITY, Integer.valueOf(request.getKeyword()), true);
            } catch (NumberFormatException e) {
                // do nothing
            }
        }
        if (request.getStatus() != null) {
            builder.with("status", SearchOperation.EQUALITY, request.getStatus(), false);
        }

        spec = builder.build();

        if (StringUtils.hasText(request.getKeyword())) {
            String productName = request.getKeyword().toLowerCase();
            Specification<OrderEntity> productNameSpec = (root, query, criteriaBuilder) -> {
                Join<OrderEntity, OrderDetailEntity> orderDetailJoin = root.join("orderDetails");
                Join<OrderDetailEntity, ProductEntity> productJoin = orderDetailJoin.join("product");
                return criteriaBuilder.like(criteriaBuilder.lower(productJoin.get("name")), "%" + productName + "%");
            };
            spec = Specification.where(spec).or(productNameSpec);
        }
        if (request.getUserId() != null) {
            List<SpecSearchCriteria> userCriteria = new ArrayList<>();
            userCriteria.add(new SpecSearchCriteria("id", SearchOperation.EQUALITY, request.getUserId(), true));
            Specification<OrderEntity> userSpec = builder.joinTableWithCondition("user", userCriteria);
            spec = Specification.where(spec).and(userSpec);

        }
        Page<MyOrderDTO> page = orderRepository.findAll(Objects.requireNonNull(spec), Paging.getPageable(request))
                .map(orderMapper::toMyOrderDTO);
        page.forEach(myOrderDTO -> myOrderDTO.setDetails(orderDetailService.findByOrderId(myOrderDTO.getId())));
        return page;
    }

    @Override
    public List<OrderDTO> findByProductId(Integer id, Date startDate, Date endDate) {
        Sort sort = Sort.by(Sort.Order.desc("orderDate"));
        return orderRepository.findAllByProductId(id, startDate, endDate, sort)
                .stream().map(orderMapper::toDTO).toList();
    }
}
