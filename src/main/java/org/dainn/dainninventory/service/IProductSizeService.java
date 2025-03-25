package org.dainn.dainninventory.service;

import org.dainn.dainninventory.dto.product.ProductSizeDTO;

import java.util.List;

public interface IProductSizeService {
    void save(List<ProductSizeDTO> list);
    void updateStock(List<ProductSizeDTO> list);
    List<ProductSizeDTO> findAllByProductCode(String code);
    ProductSizeDTO findByProductIdAndSizeId(Integer productId, Integer sizeId);
    int getStockByProductId(Integer productId);
}
