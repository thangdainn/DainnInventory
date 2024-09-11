package org.dainn.dainninventory.service.impl;

import lombok.RequiredArgsConstructor;
import org.dainn.dainninventory.dto.GoodsReceiptDetailDTO;
import org.dainn.dainninventory.dto.ProductSizeDTO;
import org.dainn.dainninventory.entity.GoodsReceiptDetailEntity;
import org.dainn.dainninventory.entity.InventoryEntity;
import org.dainn.dainninventory.entity.ProductSizeEntity;
import org.dainn.dainninventory.exception.AppException;
import org.dainn.dainninventory.exception.ErrorCode;
import org.dainn.dainninventory.mapper.IGoodReceiptDetailMapper;
import org.dainn.dainninventory.repository.*;
import org.dainn.dainninventory.service.IBaseRedisService;
import org.dainn.dainninventory.service.IGoodsReceiptDetailService;
import org.dainn.dainninventory.service.IProductSizeService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class GoodsReceiptDetailService implements IGoodsReceiptDetailService {
    private final IGoodsReceiptRepository goodsReceiptRepository;
    private final IGoodsReceiptDetailRepository goodsReceiptDetailRepository;
    private final IProductRepository productRepository;
    private final IInventoryRepository inventoryRepository;
    private final IGoodReceiptDetailMapper goodReceiptDetailMapper;
    private final IProductSizeService productSizeService;
    private final IProductSizeRepository productSizeRepository;
    private final IBaseRedisService baseRedisService;

    @Transactional
    @Override
    public void insert(List<GoodsReceiptDetailDTO> list, Integer goodReceiptId) {
        List<ProductSizeDTO> productSizeDTOList = new ArrayList<>();
        for (GoodsReceiptDetailDTO dto : list) {
            GoodsReceiptDetailEntity entity;
            entity = goodReceiptDetailMapper.toEntity(dto);
            entity.setGoodsReceipt(goodsReceiptRepository.findById(goodReceiptId)
                    .orElseThrow(() -> new AppException(ErrorCode.GOOD_RECEIPT_NOT_EXISTED)));
            entity.setProduct(productRepository.findById(dto.getProductId())
                    .orElseThrow(() -> new AppException(ErrorCode.PRODUCT_NOT_EXISTED)));
            goodsReceiptDetailRepository.save(entity);
            InventoryEntity inventoryEntity = inventoryRepository.findByProductId(dto.getProductId())
                    .orElseThrow(() -> new AppException(ErrorCode.PRODUCT_NOT_EXISTED));
            inventoryRepository.updateQuantityByProduct_Id(
                    dto.getProductId(), dto.getQuantity() + inventoryEntity.getQuantity());
            productSizeDTOList.add(ProductSizeDTO.builder()
                                                .quantity(dto.getQuantity())
                                                .productId(dto.getProductId())
                                                .sizeId(dto.getSizeId())
                                                .build());
        }
        productSizeService.insert(productSizeDTOList);
    }


    @Override
    public List<GoodsReceiptDetailDTO> findByGoodReceiptId(Integer id) {
        return goodsReceiptDetailRepository.findByGoodsReceipt_Id(id)
                .stream().map(goodReceiptDetailMapper::toDTO).toList();
    }

    @Transactional
    @Override
    public void deleteByGoodReceiptId(Integer goodReceiptId) {
        List<GoodsReceiptDetailDTO> list = findByGoodReceiptId(goodReceiptId);
        for (GoodsReceiptDetailDTO dto : list) {
            InventoryEntity inventoryEntity = inventoryRepository.findByProductId(dto.getProductId())
                    .orElseThrow(() -> new AppException(ErrorCode.PRODUCT_NOT_EXISTED));
            inventoryRepository.updateQuantityByProduct_Id(
                    dto.getProductId(), inventoryEntity.getQuantity() - dto.getQuantity());
            ProductSizeEntity productSizeEntity = productSizeRepository.findByProduct_IdAndSize_Id(dto.getProductId(), dto.getSizeId())
                    .orElseThrow(() -> new AppException(ErrorCode.PRODUCT_SIZE_NOT_EXISTED));
            productSizeRepository.updateQuantityByProduct_IdAndSize_Id(
                    dto.getProductId(), productSizeEntity.getQuantity() - dto.getQuantity(), dto.getSizeId());
        }
        baseRedisService.flushDb();
    }
}
