package org.dainn.dainninventory.service.impl;

import lombok.RequiredArgsConstructor;
import org.dainn.dainninventory.dto.order.OrderDetailDTO;
import org.dainn.dainninventory.entity.OrderDetailEntity;
import org.dainn.dainninventory.entity.OrderEntity;
import org.dainn.dainninventory.entity.ProductSizeEntity;
import org.dainn.dainninventory.exception.AppException;
import org.dainn.dainninventory.exception.ErrorCode;
import org.dainn.dainninventory.mapper.IOrderDetailMapper;
import org.dainn.dainninventory.repository.*;
import org.dainn.dainninventory.service.IBaseRedisService;
import org.dainn.dainninventory.service.IOrderDetailService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderDetailService implements IOrderDetailService {
    private final IOrderDetailRepository orderDetailRepository;
    private final IProductRepository productRepository;
    private final ISizeRepository sizeRepository;
    private final IProductSizeRepository productSizeRepository;
    private final IOrderDetailMapper orderDetailMapper;
    private final IBaseRedisService baseRedisService;

    @Transactional
    @Override
    public void insert(List<OrderDetailDTO> list, OrderEntity order) {
        for (OrderDetailDTO dto : list) {
            OrderDetailEntity entity = orderDetailMapper.toEntity(dto);
            entity.setOrder(order);
            entity.setProduct(productRepository.findById(dto.getProductId())
                    .orElseThrow(() -> new AppException(ErrorCode.PRODUCT_NOT_EXISTED)));
            entity.setSize(sizeRepository.findById(dto.getSizeId())
                    .orElseThrow(() -> new AppException(ErrorCode.SIZE_NOT_EXISTED)));
            orderDetailRepository.save(entity);
            ProductSizeEntity productSizeEntity = productSizeRepository.findByProduct_IdAndSize_Id(dto.getProductId(), dto.getSizeId())
                    .orElseThrow(() -> new AppException(ErrorCode.PRODUCT_SIZE_NOT_EXISTED));
            productSizeRepository.updateQuantityByProduct_IdAndSize_Id(productSizeEntity.getProduct().getId(),
                    productSizeEntity.getQuantity() - dto.getQuantity(), productSizeEntity.getSize().getId());
//            productSizeRepository.flush();
        }
        baseRedisService.flushDb();
    }


    @Override
    public List<OrderDetailDTO> findByOrderId(Integer orderId) {
        return orderDetailRepository.findByOrder_Id(orderId)
                .stream().map(orderDetailMapper::toDTO).toList();
    }
}
