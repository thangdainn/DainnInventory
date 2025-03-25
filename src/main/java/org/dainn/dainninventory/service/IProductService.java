package org.dainn.dainninventory.service;

import org.dainn.dainninventory.dto.product.ProductPageRequest;
import org.dainn.dainninventory.dto.product.ProductRequest;
import org.dainn.dainninventory.dto.product.ProductDTO;
import org.springframework.data.domain.Page;

import java.util.Date;
import java.util.List;

public interface IProductService {
    ProductDTO insert(ProductRequest dto);
    ProductDTO update(ProductRequest dto);
    void delete(List<Integer> ids);
    ProductDTO findById(Integer id);
    ProductDTO findByCode(String code);
    List<ProductDTO> findAll();
    List<ProductDTO> findAll(Integer status);
    List<ProductDTO> findRecentSale(Date startDate, Date endDate);

    Page<ProductDTO> findWithSpec(ProductPageRequest request);
    Page<ProductDTO> findInventoryWithSpec(ProductPageRequest request);
}
