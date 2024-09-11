package org.dainn.dainninventory.service.impl;

import com.fasterxml.jackson.core.type.TypeReference;
import lombok.RequiredArgsConstructor;
import org.dainn.dainninventory.dto.ProductSizeDTO;
import org.dainn.dainninventory.entity.InventoryEntity;
import org.dainn.dainninventory.entity.ProductSizeEntity;
import org.dainn.dainninventory.exception.AppException;
import org.dainn.dainninventory.exception.ErrorCode;
import org.dainn.dainninventory.mapper.IProductSizeMapper;
import org.dainn.dainninventory.repository.IInventoryRepository;
import org.dainn.dainninventory.repository.IProductRepository;
import org.dainn.dainninventory.repository.IProductSizeRepository;
import org.dainn.dainninventory.repository.ISizeRepository;
import org.dainn.dainninventory.service.IBaseRedisService;
import org.dainn.dainninventory.service.IProductSizeService;
import org.dainn.dainninventory.utils.constant.RedisConstant;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ProductSizeService implements IProductSizeService {
    private final IProductRepository productRepository;
    private final IProductSizeRepository productSizeRepository;
    private final ISizeRepository sizeRepository;
    private final IInventoryRepository inventoryRepository;
    private final IProductSizeMapper productSizeMapper;
    private final IBaseRedisService baseRedisService;

    @Transactional
    @Override
    public void insert(List<ProductSizeDTO> list) {
        for (ProductSizeDTO dto : list) {
            Optional<ProductSizeEntity> optional = productSizeRepository.findByProduct_IdAndSize_Id(dto.getProductId(), dto.getSizeId());
            if (optional.isPresent()){
                dto.setQuantity(dto.getQuantity() + optional.get().getQuantity());
            }
            ProductSizeEntity entity = productSizeMapper.toEntity(dto);
            entity.setSize(sizeRepository.findById(dto.getSizeId())
                    .orElseThrow(() -> new AppException(ErrorCode.SIZE_NOT_EXISTED)));
            entity.setProduct(productRepository.findById(dto.getProductId())
                    .orElseThrow(() -> new AppException(ErrorCode.PRODUCT_NOT_EXISTED)));
            productSizeRepository.save(entity);
            InventoryEntity inventoryEntity = inventoryRepository.findByProductId(dto.getProductId())
                    .orElseThrow(() -> new AppException(ErrorCode.PRODUCT_NOT_EXISTED));
            inventoryRepository.updateQuantityByProduct_Id(
                    dto.getProductId(), inventoryEntity.getQuantity() + dto.getQuantity());
        }
    }

    @Override
    public void updateQuantity(ProductSizeDTO dto) {
        productSizeRepository.updateQuantityByProduct_IdAndSize_Id(dto.getProductId(), dto.getQuantity(), dto.getSizeId());
    }

    @Override
    public List<ProductSizeDTO> findAllByProductCode(String code) {
        String key = RedisConstant.PRODUCT_SIZES_KEY_PREFIX + "::code:" + code;
        List<ProductSizeDTO> list = baseRedisService.getCache(key, new TypeReference<List<ProductSizeDTO>>() {});
        if (list == null) {
            list = productSizeRepository.findAllByProduct_Code(code)
                    .stream().map(productSizeMapper::toDTO).toList();
            list.forEach(dto -> {
                dto.setSizeName(sizeRepository.findById(dto.getSizeId())
                        .orElseThrow(() -> new AppException(ErrorCode.SIZE_NOT_EXISTED)).getName());
            });
            baseRedisService.setCache(key, list);
        }
        return list;
    }

    @Override
    public ProductSizeDTO findByProductIdAndSizeId(Integer productId, Integer sizeId) {
        String key = RedisConstant.PRODUCT_SIZES_KEY_PREFIX + "::productId:" + productId + "::sizeId:" + sizeId;
        ProductSizeDTO dto = baseRedisService.getCache(key, new TypeReference<ProductSizeDTO>() {});
        if (dto == null) {
            dto = productSizeRepository.findByProduct_IdAndSize_Id(productId, sizeId)
                    .map(productSizeMapper::toDTO)
                    .orElseThrow(() -> new AppException(ErrorCode.PRODUCT_SIZE_NOT_EXISTED));
            baseRedisService.setCache(key, dto);
        }
        return dto;
    }


//    @Override
//    public List<OrderDetailDTO> findByOrderId(Integer orderId) {
//        return orderDetailRepository.findByOrder_Id(orderId)
//                .stream().map(orderDetailMapper::toDTO).toList();
//    }

//    @Transactional
//    @Override
//    public void deleteByOrderId(Integer orderId) {
//        List<OrderDetailDTO> list = findByOrderId(orderId);
//        for (OrderDetailDTO dto : list) {
//            InventoryEntity inventoryEntity = inventoryRepository.findByProductId(dto.getProductId())
//                    .orElseThrow(() -> new AppException(ErrorCode.PRODUCT_NOT_EXISTED));
//            inventoryRepository.updateQuantityByProduct_Id(
//                    dto.getProductId(), inventoryEntity.getQuantity() + dto.getQuantity());
//        }
//        baseRedisService.flushDb();
//    }
}
