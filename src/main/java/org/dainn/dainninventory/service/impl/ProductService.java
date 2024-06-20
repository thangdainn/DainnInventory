package org.dainn.dainninventory.service.impl;

import com.cloudinary.Cloudinary;
import lombok.RequiredArgsConstructor;
import org.dainn.dainninventory.controller.request.ProductPageRequest;
import org.dainn.dainninventory.controller.request.ProductRequest;
import org.dainn.dainninventory.controller.request.UserPageRequest;
import org.dainn.dainninventory.dto.BrandDTO;
import org.dainn.dainninventory.dto.InventoryDTO;
import org.dainn.dainninventory.dto.ProductDTO;
import org.dainn.dainninventory.entity.*;
import org.dainn.dainninventory.exception.AppException;
import org.dainn.dainninventory.exception.ErrorCode;
import org.dainn.dainninventory.mapper.IBrandMapper;
import org.dainn.dainninventory.mapper.IInventoryMapper;
import org.dainn.dainninventory.mapper.IProductMapper;
import org.dainn.dainninventory.repository.IBrandRepository;
import org.dainn.dainninventory.repository.ICategoryRepository;
import org.dainn.dainninventory.repository.IImageRepository;
import org.dainn.dainninventory.repository.IProductRepository;
import org.dainn.dainninventory.service.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.awt.*;
import java.util.List;

@Service
@RequiredArgsConstructor

public class ProductService implements IProductService {
    private final IProductRepository productRepository;
    private final IImageService imageService;
    private final IProductMapper productMapper;
    private final IInventoryMapper inventoryMapper;
    private final ICategoryRepository categoryRepository;
    private final IBrandRepository brandRepository;
    private final IInventoryService inventoryService;
    @Transactional
    @Override
    public ProductDTO save(ProductRequest request, MultipartFile mainImg, List<MultipartFile> subImg) {

        ProductEntity productEntity;
        ProductDTO dto = productMapper.toDTO(request);
        if (dto.getId() != null) {

            ProductEntity old = productRepository.findById(dto.getId())
                    .orElseThrow(() -> new AppException(ErrorCode.PRODUCT_NOT_EXISTED));
            if (!old.getCode().equals(dto.getCode()) && productRepository.existsByCode(request.getCode())) {
                throw new AppException(ErrorCode.PRODUCT_CODE_EXISTED);
            }
            if (!old.getName().equals(dto.getName()) && productRepository.existsByName(request.getName())) {
                throw new AppException(ErrorCode.PRODUCT_NAME_EXISTED);
            }
            if (mainImg == null) {
                dto.setImgUrl(old.getImgUrl());
            } else {
                dto.setImgUrl(imageService.uploadImage(mainImg));
            }
            productEntity = productMapper.updateEntity(old, dto);
        } else {
            if (productRepository.existsByCode(request.getCode())) {
                throw new AppException(ErrorCode.PRODUCT_CODE_EXISTED);
            }
            if (productRepository.existsByName(request.getName())) {
                throw new AppException(ErrorCode.PRODUCT_NAME_EXISTED);
            }
            productEntity = productMapper.toEntity(dto);
            productEntity.setImgUrl(imageService.uploadImage(mainImg));
        }
        productEntity.setCategory(categoryRepository.findById(dto.getCategoryId())
                .orElseThrow(() -> new AppException(ErrorCode.CATEGORY_NOT_EXISTED)));
        productEntity.setBrand(brandRepository.findById(dto.getBrandId())
                .orElseThrow(() -> new AppException(ErrorCode.BRAND_NOT_EXISTED)));
        productEntity = productRepository.save(productEntity);
        if (subImg != null && !subImg.isEmpty()) {
            imageService.deleteByProductId(productEntity.getId());
            imageService.uploadImages(subImg, productEntity);
        }
        InventoryDTO inventoryDTO = inventoryMapper.toDTO(request);
        inventoryDTO.setProductId(productEntity.getId());
        inventoryService.save(inventoryDTO);
        return productMapper.toDTO(productEntity);
    }

    @Transactional
    @Override
    public void delete(List<Integer> ids) {
        productRepository.deleteAllByIdInBatchCustom(ids);
    }

    @Override
    public ProductDTO findById(Integer id) {
        return productMapper.toDTO(productRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.PRODUCT_NOT_EXISTED)));
    }

    @Override
    public ProductDTO findByCode(String code) {
        return productMapper.toDTO(productRepository.findByCode(code)
                .orElseThrow(() -> new AppException(ErrorCode.PRODUCT_NOT_EXISTED)));
    }

    @Override
    public List<ProductDTO> findAll() {
        return productRepository.findAll()
                .stream().map(productMapper::toDTO).toList();
    }

    @Override
    public Page<ProductDTO> findAll(ProductPageRequest request) {
        Sort sort;
        if (request.getSortBy() != null && !request.getSortBy().isBlank()) {
            sort = request.getSortBy().equalsIgnoreCase(Sort.Direction.ASC.name())
                    ? Sort.by(request.getSortBy()).ascending() : Sort.by(request.getSortBy()).descending();
        } else {
            sort = Sort.unsorted();
        }
        Pageable pageable = PageRequest.of(request.getPage(), request.getSize(), sort);
        Page<ProductEntity> entityPage;
        if (request.getKeyword() != null) {
            entityPage = productRepository.findByDynamicFilters(
                    request.getKeyword(), request.getBrandId(), request.getCategoryId(), request.getStatus(), pageable);
        } else {
            entityPage = productRepository.findAll(pageable);
        }
        return entityPage.map(productMapper::toDTO);
    }
}
