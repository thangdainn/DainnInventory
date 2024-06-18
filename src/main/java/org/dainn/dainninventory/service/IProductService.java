package org.dainn.dainninventory.service;

import org.dainn.dainninventory.controller.request.ProductPageRequest;
import org.dainn.dainninventory.controller.request.UserPageRequest;
import org.dainn.dainninventory.dto.BrandDTO;
import org.dainn.dainninventory.dto.ProductDTO;
//import org.hibernate.query.Page;
import org.springframework.data.domain.Page;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface IProductService {
    ProductDTO save(ProductDTO dto, MultipartFile mainImg, List<MultipartFile> subImg);
    void delete(List<Integer> ids);
    ProductDTO findById(Integer id);
    ProductDTO findByCode(String code);
    List<ProductDTO> findAll();

    Page<ProductDTO> findAll(ProductPageRequest request);
}
