package org.dainn.dainninventory.service.impl;

import com.fasterxml.jackson.core.type.TypeReference;
import lombok.RequiredArgsConstructor;
import org.dainn.dainninventory.dto.product.ProductPageRequest;
import org.dainn.dainninventory.dto.product.ProductRequest;
import org.dainn.dainninventory.dto.image.ImageDTO;
import org.dainn.dainninventory.dto.product.ProductDTO;
import org.dainn.dainninventory.entity.ProductEntity;
import org.dainn.dainninventory.exception.AppException;
import org.dainn.dainninventory.exception.ErrorCode;
import org.dainn.dainninventory.mapper.IProductMapper;
import org.dainn.dainninventory.repository.IBrandRepository;
import org.dainn.dainninventory.repository.ICategoryRepository;
import org.dainn.dainninventory.repository.IProductRepository;
import org.dainn.dainninventory.repository.specification.SearchOperation;
import org.dainn.dainninventory.repository.specification.SpecSearchCriteria;
import org.dainn.dainninventory.repository.specification.SpecificationBuilder;
import org.dainn.dainninventory.service.IBaseRedisService;
import org.dainn.dainninventory.service.IImageService;
import org.dainn.dainninventory.service.IProductService;
import org.dainn.dainninventory.service.IProductSizeService;
import org.dainn.dainninventory.utils.Paging;
import org.dainn.dainninventory.utils.constant.RedisConstant;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.text.Normalizer;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.regex.Pattern;

@Service
@RequiredArgsConstructor

public class ProductService implements IProductService {
    private final IProductRepository productRepository;
    private final IImageService imageService;
    private final IProductMapper productMapper;
    private final ICategoryRepository categoryRepository;
    private final IBrandRepository brandRepository;
    private final IBaseRedisService baseRedisService;
    private final IProductSizeService productSizeService;

    @Transactional
    @Override
    public ProductDTO insert(ProductRequest request) {
        ProductDTO dto = productMapper.toDTO(request);
        if (productRepository.existsByName(request.getName())) {
            throw new AppException(ErrorCode.PRODUCT_NAME_EXISTED);
        }
        ProductEntity productEntity = productMapper.toEntity(dto);
        productEntity = setConditions(productEntity, dto);
        dto = productMapper.toDTO(productEntity);
        dto.setImageUrls(imageService.findByProductId(dto.getId())
                .stream().map(ImageDTO::getUrl).toList());

        String key = RedisConstant.PRODUCT_KEY_PREFIX + "::id:" + dto.getId();
        baseRedisService.flushDb();
        baseRedisService.setCache(key, dto);
        return dto;
    }

    @Transactional
    @Override
    public ProductDTO update(ProductRequest request) {
        ProductEntity productEntity;
        ProductDTO dto = productMapper.toDTO(request);
        ProductEntity old = productRepository.findById(dto.getId())
                .orElseThrow(() -> new AppException(ErrorCode.PRODUCT_NOT_EXISTED));
        if (!old.getName().equals(dto.getName()) && productRepository.existsByName(request.getName())) {
            throw new AppException(ErrorCode.PRODUCT_NAME_EXISTED);
        }
        productEntity = productMapper.updateEntity(old, dto);
        productEntity = setConditions(productEntity, dto);
        dto = productMapper.toDTO(productEntity);
        dto.setImageUrls(imageService.findByProductId(dto.getId())
                .stream().map(ImageDTO::getUrl).toList());

        String key = RedisConstant.PRODUCT_KEY_PREFIX + "::id:" + dto.getId();
        baseRedisService.flushDb();
        baseRedisService.setCache(key, dto);
        return dto;
    }

    private ProductEntity setConditions(ProductEntity entity, ProductDTO dto) {
        entity.setCode(generateCodeFromName(dto.getName()));
        entity.setCategory(categoryRepository.findById(dto.getCategoryId())
                .orElseThrow(() -> new AppException(ErrorCode.CATEGORY_NOT_EXISTED)));
        entity.setBrand(brandRepository.findById(dto.getBrandId())
                .orElseThrow(() -> new AppException(ErrorCode.BRAND_NOT_EXISTED)));
        entity = productRepository.save(entity);
        if (dto.getImageUrls() != null && !dto.getImageUrls().isEmpty()) {
            imageService.deleteByProductId(entity.getId());
            for (String url : dto.getImageUrls()) {
                imageService.save(url, entity);
            }
        }
        return entity;
    }

    private String generateCodeFromName(String name) {
        String noAccent = Normalizer.normalize(name, Normalizer.Form.NFD);
        Pattern pattern = Pattern.compile("\\p{InCombiningDiacriticalMarks}+");
        String noSpecialChar = pattern.matcher(noAccent).replaceAll("");
        return noSpecialChar.toLowerCase().replaceAll("\\s+", "-");
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
        ProductDTO dto = baseRedisService.getCache(key, new TypeReference<>() {
        });
        if (dto == null) {
            dto = productMapper.toDTO(productRepository.findById(id)
                    .orElseThrow(() -> new AppException(ErrorCode.PRODUCT_NOT_EXISTED)));
            baseRedisService.setCache(key, dto);
        }
        return dto;
    }

    @Override
    public ProductDTO findByCode(String code) {
        String key = RedisConstant.PRODUCT_KEY_PREFIX + "::code:" + code;
        ProductDTO dto = baseRedisService.getCache(key, new TypeReference<>() {
        });
        if (dto == null) {
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
        List<ProductDTO> list = baseRedisService.getCache(key, new TypeReference<>() {
        });
        if (list == null) {
            list = productRepository.findAll()
                    .stream().map(productMapper::toDTO).toList();
            baseRedisService.setCache(key, list);
        }
        return list;
    }

    @Override
    public List<ProductDTO> findAll(Integer status) {
        String key = RedisConstant.PRODUCTS_KEY_PREFIX + "::status:" + status;
        List<ProductDTO> list = baseRedisService.getCache(key, new TypeReference<>() {
        });
        if (list == null) {
            list = productRepository.findAllByStatus(status)
                    .stream().map(productMapper::toDTO).toList();
            baseRedisService.setCache(key, list);
        }
        return list;
    }

    @Override
    public List<ProductDTO> findRecentSale(Date startDate, Date endDate) {
        return productRepository.getRecentSale(startDate, endDate).stream().map(productMapper::toDTO).toList();
    }

    @Override
    public Page<ProductDTO> findWithSpec(ProductPageRequest request) {
        String key = RedisConstant.PRODUCTS_KEY_PREFIX + "::page:" + request.getPage() + "::size:" + request.getSize()
                + "::sort:" + request.getSortBy() + "::dir:" + request.getSortDir() + "::keyword:" + request.getKeyword()
                + "::minPrice:" + request.getMinPrice() + "::maxPrice:" + request.getMaxPrice() + "::status:" + request.getStatus()
                + "::categoryIds:" + request.getCategoryIds() + "::brandIds:" + request.getBrandIds();
        Page<ProductDTO> pageDTO = baseRedisService.getCache(key, new TypeReference<>() {
        });
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

    @Override
    public Page<ProductDTO> findInventoryWithSpec(ProductPageRequest request) {
        SpecificationBuilder<ProductEntity> builder = new SpecificationBuilder<>();
        Page<ProductEntity> page;
        Specification<ProductEntity> spec;

        if (StringUtils.hasText(request.getKeyword())) {
            builder.with("name", SearchOperation.CONTAINS, request.getKeyword(), false);
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
        return page.map((item) -> {
            ProductDTO dto = productMapper.toDTO(item);
            dto.setStock(productSizeService.getStockByProductId(dto.getId()));
            return dto;
        });
    }

    private boolean isNonNullOrNonEmpty(List<Integer> list) {
        return list != null && !list.isEmpty();
    }
}
