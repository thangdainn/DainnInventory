package org.dainn.dainninventory.service.impl;

import lombok.RequiredArgsConstructor;
import org.dainn.dainninventory.controller.request.ProductPageRequest;
import org.dainn.dainninventory.controller.request.ProductRequest;
import org.dainn.dainninventory.dto.InventoryDTO;
import org.dainn.dainninventory.dto.ProductDTO;
import org.dainn.dainninventory.entity.InventoryEntity;
import org.dainn.dainninventory.entity.ProductEntity;
import org.dainn.dainninventory.exception.AppException;
import org.dainn.dainninventory.exception.ErrorCode;
import org.dainn.dainninventory.mapper.IInventoryMapper;
import org.dainn.dainninventory.mapper.IProductMapper;
import org.dainn.dainninventory.repository.IInventoryRepository;
import org.dainn.dainninventory.repository.IProductRepository;
import org.dainn.dainninventory.service.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor

public class InventoryService implements IInventoryService {
    private final IInventoryMapper inventoryMapper;
    private final IInventoryRepository inventoryRepository;
    private final IProductRepository productRepository;

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
        return inventoryMapper.toDTO(inventoryRepository.save(inventoryEntity));
    }
}
