package org.dainn.dainninventory.service.impl;

import lombok.RequiredArgsConstructor;
import org.dainn.dainninventory.dto.InventoryDTO;
import org.dainn.dainninventory.entity.InventoryEntity;
import org.dainn.dainninventory.entity.ProductEntity;
import org.dainn.dainninventory.exception.AppException;
import org.dainn.dainninventory.exception.ErrorCode;
import org.dainn.dainninventory.mapper.IInventoryMapper;
import org.dainn.dainninventory.repository.IInventoryRepository;
import org.dainn.dainninventory.repository.IProductRepository;
import org.dainn.dainninventory.service.IBaseRedisService;
import org.dainn.dainninventory.service.IInventoryService;
import org.dainn.dainninventory.utils.constant.RedisConstant;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor

public class InventoryService implements IInventoryService {
    private final IInventoryMapper inventoryMapper;
    private final IInventoryRepository inventoryRepository;
    private final IProductRepository productRepository;
    private final IBaseRedisService baseRedisService;

    @Transactional
    @Override
    public InventoryDTO save(InventoryDTO dto) {
        InventoryEntity inventoryEntity;
        ProductEntity productEntity = productRepository.findById(dto.getProductId())
                .orElseThrow(() -> new AppException(ErrorCode.PRODUCT_NOT_EXISTED));
        Optional<InventoryEntity> optional = inventoryRepository.findByProductId(dto.getProductId());
        if (optional.isPresent()) {
            inventoryEntity = inventoryMapper.updateEntity(optional.get(), dto);
        } else {
            inventoryEntity = inventoryMapper.toEntity(dto);
        }
        inventoryEntity.setProduct(productEntity);
        dto = inventoryMapper.toDTO(inventoryRepository.save(inventoryEntity));
        String key = RedisConstant.INVENTORY_KEY_PREFIX + "::id:" + dto.getId();
        baseRedisService.setCache(key, dto);
        return dto;
    }
}
