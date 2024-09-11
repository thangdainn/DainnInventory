package org.dainn.dainninventory.service;

import org.dainn.dainninventory.dto.OrderDetailDTO;
import org.dainn.dainninventory.dto.ProductSizeDTO;

import java.util.List;

public interface IProductSizeService {
    void insert(List<ProductSizeDTO> list);
    void updateQuantity(ProductSizeDTO dto);
    List<ProductSizeDTO> findAllByProductCode(String code);
    ProductSizeDTO findByProductIdAndSizeId(Integer productId, Integer sizeId);
//    List<OrderDetailDTO> findByOrderId(Integer orderId);
//    void deleteByOrderId(Integer orderId);
}
