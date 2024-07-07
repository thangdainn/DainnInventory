package org.dainn.dainninventory.service;

import org.dainn.dainninventory.controller.request.ProductPageRequest;
import org.dainn.dainninventory.controller.request.ProductRequest;
import org.dainn.dainninventory.dto.ProductDTO;
import org.springframework.data.domain.Page;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface IProductService {
    ProductDTO insert(ProductRequest dto, MultipartFile mainImg, List<MultipartFile> subImg);
    ProductDTO update(ProductRequest dto, MultipartFile mainImg, List<MultipartFile> subImg);
    void delete(List<Integer> ids);
    ProductDTO findById(Integer id);
    ProductDTO findByCode(String code);
    List<ProductDTO> findAll();
    List<ProductDTO> findAll(Integer status);

    Page<ProductDTO> findWithSpec(ProductPageRequest request);
}
