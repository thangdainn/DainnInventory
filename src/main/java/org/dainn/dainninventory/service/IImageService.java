package org.dainn.dainninventory.service;

import org.dainn.dainninventory.dto.ImageDTO;
import org.dainn.dainninventory.entity.ImageEntity;
import org.dainn.dainninventory.entity.ProductEntity;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface IImageService {
    ImageEntity save(String url, ProductEntity productEntity);
    String uploadImage(MultipartFile image);
    void uploadImages(List<MultipartFile> images, ProductEntity productEntity);
    List<ImageDTO> findByProductId(Integer productId);
    void deleteByProductId(Integer productId);
}
