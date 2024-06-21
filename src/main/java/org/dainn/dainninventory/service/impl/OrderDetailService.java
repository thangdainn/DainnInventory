package org.dainn.dainninventory.service.impl;

import lombok.RequiredArgsConstructor;
import org.dainn.dainninventory.dto.OrderDetailDTO;
import org.dainn.dainninventory.entity.GoodsReceiptDetailEntity;
import org.dainn.dainninventory.entity.InventoryEntity;
import org.dainn.dainninventory.entity.OrderDetailEntity;
import org.dainn.dainninventory.exception.AppException;
import org.dainn.dainninventory.exception.ErrorCode;
import org.dainn.dainninventory.mapper.IOrderDetailMapper;
import org.dainn.dainninventory.repository.IInventoryRepository;
import org.dainn.dainninventory.repository.IOrderDetailRepository;
import org.dainn.dainninventory.repository.IOrderRepository;
import org.dainn.dainninventory.repository.IProductRepository;
import org.dainn.dainninventory.service.IOrderDetailService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderDetailService implements IOrderDetailService {
    private final IOrderRepository orderRepository;
    private final IOrderDetailRepository orderDetailRepository;
    private final IProductRepository productRepository;
    private final IInventoryRepository inventoryRepository;
    private final IOrderDetailMapper orderDetailMapper;

    @Transactional
    @Override
    public void insert(List<OrderDetailDTO> list, Integer orderId) {
        for (OrderDetailDTO dto : list) {
            OrderDetailEntity entity = orderDetailMapper.toEntity(dto);
            entity.setOrder(orderRepository.findById(orderId)
                    .orElseThrow(() -> new AppException(ErrorCode.ORDER_NOT_EXISTED)));
            entity.setProduct(productRepository.findById(dto.getProductId())
                    .orElseThrow(() -> new AppException(ErrorCode.PRODUCT_NOT_EXISTED)));
            orderDetailRepository.save(entity);
            InventoryEntity inventoryEntity = inventoryRepository.findByProductId(dto.getProductId())
                    .orElseThrow(() -> new AppException(ErrorCode.PRODUCT_NOT_EXISTED));
            inventoryRepository.updateQuantityByProduct_Id(
                    dto.getProductId(), inventoryEntity.getQuantity() - dto.getQuantity());
        }
    }


    @Override
    public List<OrderDetailDTO> findByOrderId(Integer orderId) {
        return orderDetailRepository.findByOrder_Id(orderId)
                .stream().map(orderDetailMapper::toDTO).toList();
    }

    @Transactional
    @Override
    public void deleteByOrderId(Integer orderId) {
        List<OrderDetailDTO> list = findByOrderId(orderId);
        for (OrderDetailDTO dto : list) {
            InventoryEntity inventoryEntity = inventoryRepository.findByProductId(dto.getProductId())
                    .orElseThrow(() -> new AppException(ErrorCode.PRODUCT_NOT_EXISTED));
            inventoryRepository.updateQuantityByProduct_Id(
                    dto.getProductId(), inventoryEntity.getQuantity() + dto.getQuantity());
        }
    }
}
