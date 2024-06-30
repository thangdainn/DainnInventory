package org.dainn.dainninventory.service.impl;

import lombok.RequiredArgsConstructor;
import org.dainn.dainninventory.controller.request.ProductPageRequest;
import org.dainn.dainninventory.controller.request.ProductRequest;
import org.dainn.dainninventory.dto.ImageDTO;
import org.dainn.dainninventory.dto.InventoryDTO;
import org.dainn.dainninventory.dto.ProductDTO;
import org.dainn.dainninventory.entity.ProductEntity;
import org.dainn.dainninventory.exception.AppException;
import org.dainn.dainninventory.exception.ErrorCode;
import org.dainn.dainninventory.mapper.IInventoryMapper;
import org.dainn.dainninventory.mapper.IProductMapper;
import org.dainn.dainninventory.repository.IBrandRepository;
import org.dainn.dainninventory.repository.ICategoryRepository;
import org.dainn.dainninventory.repository.IProductRepository;
import org.dainn.dainninventory.repository.specification.SearchOperation;
import org.dainn.dainninventory.repository.specification.SpecSearchCriteria;
import org.dainn.dainninventory.repository.specification.SpecificationBuilder;
import org.dainn.dainninventory.service.IImageService;
import org.dainn.dainninventory.service.IInventoryService;
import org.dainn.dainninventory.service.IProductService;
import org.dainn.dainninventory.utils.Paging;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

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
        Optional<ProductEntity> optional = productRepository.findByCode(code);
        if (optional.isEmpty()) {
            throw new AppException(ErrorCode.PRODUCT_NOT_EXISTED);
        }
        ProductDTO dto = productMapper.toDTO(optional.get());
        dto.setImageUrls(imageService.findByProductId(dto.getId())
                .stream().map(ImageDTO::getUrl).toList());
        return dto;
    }

    @Override
    public List<ProductDTO> findAll() {
        return productRepository.findAll()
                .stream().map(productMapper::toDTO).toList();
    }

    @Override
    public List<ProductDTO> findAll(Integer status) {
        return productRepository.findAllByStatus(status)
                .stream().map(productMapper::toDTO).toList();
    }

    @Override
    public Page<ProductDTO> findWithSpec(ProductPageRequest request) {
        SpecificationBuilder<ProductEntity> builder = new SpecificationBuilder<>();
        Page<ProductEntity> page;
        Specification<ProductEntity> spec;

        if (StringUtils.hasText(request.getKeyword())) {
            builder.with("name", SearchOperation.CONTAINS, request.getKeyword(), false);
        }
        if (request.getMinPrice() != null) {
            builder.with("price", SearchOperation.GREATER_THAN_OR_EQUAL, request.getMinPrice(), false);
        }
        if (request.getMaxPrice() != null) {
            builder.with("price", SearchOperation.LESS_THAN_OR_EQUAL, request.getMaxPrice(), false);
        }
        builder.with("status", SearchOperation.EQUALITY, request.getStatus(), false);
        spec = builder.build();
        List<SpecSearchCriteria> categoryCriteria = new ArrayList<>();
        List<SpecSearchCriteria> brandCriteria = new ArrayList<>();
        Specification<ProductEntity> categorySpec = null;
        Specification<ProductEntity> brandSpec = null;
        if (isNonNullOrNonEmpty(request.getCategoryIds())) {
            for (Integer categoryId : request.getCategoryIds()) {
                categoryCriteria.add(new SpecSearchCriteria("id", SearchOperation.EQUALITY, categoryId, true));
            }
            categorySpec = builder.joinTableWithCondition("category", categoryCriteria);
        }
        if (isNonNullOrNonEmpty(request.getBrandIds())) {
            for (Integer brandId : request.getBrandIds()) {
                brandCriteria.add(new SpecSearchCriteria("id", SearchOperation.EQUALITY, brandId, true));
            }
            brandSpec = builder.joinTableWithCondition("brand", brandCriteria);
        }
        if (!categoryCriteria.isEmpty() && !brandCriteria.isEmpty()) {
            spec = Specification.where(spec).and(categorySpec).and(brandSpec);
        } else if (!categoryCriteria.isEmpty()) {
            spec = Specification.where(spec).and(categorySpec);
        } else if (!brandCriteria.isEmpty()) {
            spec = Specification.where(spec).and(brandSpec);
        }
        page = productRepository.findAll(Objects.requireNonNull(spec), Paging.getPageable(request));
        return page.map(productMapper::toDTO);
    }

    private boolean isNonNullOrNonEmpty(List<Integer> list) {
        return list != null && !list.isEmpty();
    }
}
