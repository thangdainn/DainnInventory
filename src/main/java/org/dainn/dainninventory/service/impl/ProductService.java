package org.dainn.dainninventory.service.impl;

import com.fasterxml.jackson.core.type.TypeReference;
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
import org.dainn.dainninventory.service.IBaseRedisService;
import org.dainn.dainninventory.service.IImageService;
import org.dainn.dainninventory.service.IInventoryService;
import org.dainn.dainninventory.service.IProductService;
import org.dainn.dainninventory.utils.Paging;
import org.dainn.dainninventory.utils.constant.RedisConstant;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

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
    private final IBaseRedisService baseRedisService;

    @Transactional
    @Override
    public ProductDTO insert(ProductRequest request, MultipartFile mainImg, List<MultipartFile> subImg) {
        ProductDTO dto = productMapper.toDTO(request);
        if (productRepository.existsByCode(request.getCode())) {
            throw new AppException(ErrorCode.PRODUCT_CODE_EXISTED);
        }
        if (productRepository.existsByName(request.getName())) {
            throw new AppException(ErrorCode.PRODUCT_NAME_EXISTED);
        }
        ProductEntity productEntity = productMapper.toEntity(dto);
        productEntity.setImgUrl(imageService.uploadImage(mainImg));
        productEntity = setConditions(request, productEntity, dto, subImg);
        dto = productMapper.toDTO(productEntity);
        dto.setImageUrls(imageService.findByProductId(dto.getId())
                .stream().map(ImageDTO::getUrl).toList());

        String key = RedisConstant.PRODUCT_KEY_PREFIX + "::id:" + dto.getId();
        baseRedisService.setCache(key, dto);
        return dto;
    }

    @Transactional
    @Override
    public ProductDTO update(ProductRequest request, MultipartFile mainImg, List<MultipartFile> subImg) {
        ProductEntity productEntity;
        ProductDTO dto = productMapper.toDTO(request);
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
        productEntity = setConditions(request, productEntity, dto, subImg);
        dto = productMapper.toDTO(productEntity);
        dto.setImageUrls(imageService.findByProductId(dto.getId())
                .stream().map(ImageDTO::getUrl).toList());

        String key = RedisConstant.PRODUCT_KEY_PREFIX + "::id:" + dto.getId();
        baseRedisService.flushDb();
        baseRedisService.setCache(key, dto);
        return dto;
    }

    private ProductEntity setConditions(ProductRequest request, ProductEntity entity, ProductDTO dto, List<MultipartFile> subImg){
        entity.setCategory(categoryRepository.findById(dto.getCategoryId())
                .orElseThrow(() -> new AppException(ErrorCode.CATEGORY_NOT_EXISTED)));
        entity.setBrand(brandRepository.findById(dto.getBrandId())
                .orElseThrow(() -> new AppException(ErrorCode.BRAND_NOT_EXISTED)));
        entity = productRepository.save(entity);
        if (subImg != null && !subImg.isEmpty()) {
            imageService.deleteByProductId(entity.getId());
            imageService.uploadImages(subImg, entity);
        }
        InventoryDTO inventoryDTO = inventoryMapper.toDTO(request);
        inventoryDTO.setProductId(entity.getId());
        inventoryService.save(inventoryDTO);
        return entity;
    }

    @Transactional
    @Override
    public void delete(List<Integer> ids) {
        productRepository.deleteAllByIdInBatchCustom(ids);
        baseRedisService.flushDb();
    }

    @Override
    public ProductDTO findById(Integer id) {
        String key = RedisConstant.PRODUCT_KEY_PREFIX + "::id:" + id;
        ProductDTO dto = baseRedisService.getCache(key, new TypeReference<ProductDTO>() {});
        if (dto == null){
            dto = productMapper.toDTO(productRepository.findById(id)
                    .orElseThrow(() -> new AppException(ErrorCode.PRODUCT_NOT_EXISTED)));
            baseRedisService.setCache(key, dto);
        }
        return dto;
    }

    @Override
    public ProductDTO findByCode(String code) {
        String key = RedisConstant.PRODUCT_KEY_PREFIX + "::code:" + code;
        ProductDTO dto = baseRedisService.getCache(key, new TypeReference<ProductDTO>() {});
        if (dto == null){
            ProductEntity entity = productRepository.findByCode(code)
                    .orElseThrow(() -> new AppException(ErrorCode.PRODUCT_NOT_EXISTED));
            dto = productMapper.toDTO(entity);
            dto.setImageUrls(imageService.findByProductId(dto.getId())
                    .stream().map(ImageDTO::getUrl).toList());
            baseRedisService.setCache(key, dto);
        }
        return dto;
    }

    @Override
    public List<ProductDTO> findAll() {
        String key = RedisConstant.PRODUCTS_KEY_PREFIX;
        List<ProductDTO> list = baseRedisService.getCache(key, new TypeReference<List<ProductDTO>>() {});
        if (list == null){
            list = productRepository.findAll()
                    .stream().map(productMapper::toDTO).toList();
            baseRedisService.setCache(key, list);
        }
        return list;
    }

    @Override
    public List<ProductDTO> findAll(Integer status) {
        String key = RedisConstant.PRODUCTS_KEY_PREFIX + "::status:" + status;
        List<ProductDTO> list = baseRedisService.getCache(key, new TypeReference<List<ProductDTO>>() {});
        if (list == null) {
            list = productRepository.findAllByStatus(status)
                    .stream().map(productMapper::toDTO).toList();
            baseRedisService.setCache(key, list);
        }
        return list;
    }

    @Override
    public Page<ProductDTO> findWithSpec(ProductPageRequest request) {
        String key = RedisConstant.PRODUCTS_KEY_PREFIX + "::page:" + request.getPage() + "::size:" + request.getSize()
                + "::sort:" + request.getSortBy() + "::dir:" + request.getSortDir() + "::keyword:" + request.getKeyword()
                + "::minPrice:" + request.getMinPrice() + "::maxPrice:" + request.getMaxPrice() + "::status:" + request.getStatus()
                + "::categoryIds:" + request.getCategoryIds() + "::brandIds:" + request.getBrandIds();
        Page<ProductDTO> pageDTO = baseRedisService.getCache(key, new TypeReference<Page<ProductDTO>>() {});
        if (pageDTO != null) {
            return pageDTO;
        }
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
        pageDTO = page.map(productMapper::toDTO);
        baseRedisService.setCache(key, pageDTO);
        return pageDTO;
    }

    private boolean isNonNullOrNonEmpty(List<Integer> list) {
        return list != null && !list.isEmpty();
    }
}
