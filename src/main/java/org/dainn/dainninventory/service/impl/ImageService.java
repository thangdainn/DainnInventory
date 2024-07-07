package org.dainn.dainninventory.service.impl;

import com.cloudinary.Cloudinary;
import lombok.RequiredArgsConstructor;
import org.dainn.dainninventory.dto.ImageDTO;
import org.dainn.dainninventory.entity.ImageEntity;
import org.dainn.dainninventory.entity.ProductEntity;
import org.dainn.dainninventory.exception.AppException;
import org.dainn.dainninventory.exception.ErrorCode;
import org.dainn.dainninventory.mapper.IImageMapper;
import org.dainn.dainninventory.repository.IImageRepository;
import org.dainn.dainninventory.service.IBaseRedisService;
import org.dainn.dainninventory.service.IImageService;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ImageService implements IImageService {
    private final IImageRepository imageRepository;
    private final IImageMapper imageMapper;
    private final Cloudinary cloudinary;
    private final IBaseRedisService baseRedisService;

    @Override
    public ImageEntity save(String url, ProductEntity productEntity) {
        ImageEntity imageEntity = new ImageEntity();
        imageEntity.setUrl(url);
        imageEntity.setProduct(productEntity);
        return imageRepository.save(imageEntity);
    }

    @Override
    public String uploadImage(MultipartFile image) {
        try {
            return cloudinary.uploader().upload(image.getBytes(), null).get("secure_url").toString();
        } catch (Exception e) {
            throw new AppException(ErrorCode.UPLOAD_IMAGE_FAILED);
        }
    }

    @Override
    public void uploadImages(List<MultipartFile> images, ProductEntity productEntity) {
        for (MultipartFile image : images) {
            save(uploadImage(image), productEntity);
        }
    }

    @Override
    public List<ImageDTO> findByProductId(Integer productId) {
        List<ImageEntity> entities = imageRepository.findByProductId(productId);
        return entities.stream().map(imageMapper::toDTO).toList();
    }

    @Override
    public void deleteByProductId(Integer productId) {
        imageRepository.deleteByProductId(productId);
        baseRedisService.flushDb();
    }
}
