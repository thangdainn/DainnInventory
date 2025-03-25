package org.dainn.dainninventory.service.impl;

import lombok.RequiredArgsConstructor;
import org.dainn.dainninventory.dto.product.ProductSizeDTO;
import org.dainn.dainninventory.entity.ProductEntity;
import org.dainn.dainninventory.entity.ProductSizeEntity;
import org.dainn.dainninventory.exception.AppException;
import org.dainn.dainninventory.exception.ErrorCode;
import org.dainn.dainninventory.mapper.IProductSizeMapper;
import org.dainn.dainninventory.repository.IProductRepository;
import org.dainn.dainninventory.repository.IProductSizeRepository;
import org.dainn.dainninventory.repository.ISizeRepository;
import org.dainn.dainninventory.service.IProductSizeService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductSizeService implements IProductSizeService {
    private final IProductRepository productRepository;
    private final IProductSizeRepository productSizeRepository;
    private final ISizeRepository sizeRepository;
    private final IProductSizeMapper productSizeMapper;

    @Transactional
    @Override
    public void save(List<ProductSizeDTO> list) {
        List<ProductSizeEntity> existing = productSizeRepository.findAllByProduct_Id(list.get(0).getProductId());
        ProductEntity product = productRepository.findById(list.get(0).getProductId())
                .orElseThrow(() -> new AppException(ErrorCode.PRODUCT_NOT_EXISTED));
        for (ProductSizeDTO dto : list) {
            boolean isExist = false;
            for (ProductSizeEntity entity : existing) {
                if (entity.getSize().getId().equals(dto.getSizeId())) {
                    isExist = true;
                    entity.setQuantity(dto.getQuantity());
                    productSizeRepository.save(entity);
                    break;
                }
            }
            if (!isExist) {
                ProductSizeEntity entity = productSizeMapper.toEntity(dto);
                entity.setSize(sizeRepository.findById(dto.getSizeId())
                        .orElseThrow(() -> new AppException(ErrorCode.SIZE_NOT_EXISTED)));
                entity.setProduct(product);
                productSizeRepository.save(entity);
            }
        }
    }

    @Transactional
    @Override
    public void updateStock(List<ProductSizeDTO> list) {
        for (ProductSizeDTO dto : list) {
            ProductSizeEntity entity = productSizeRepository.findById(dto.getId())
                    .orElseThrow(() -> new AppException(ErrorCode.PRODUCT_SIZE_NOT_EXISTED));
            entity.setQuantity(dto.getQuantity());
            productSizeRepository.save(entity);
        }
    }

    @Override
    public List<ProductSizeDTO> findAllByProductCode(String code) {
        return productSizeRepository.findAllByProduct_Code(code)
                .stream().map(productSizeMapper::toDTO).toList();
    }

    @Override
    public ProductSizeDTO findByProductIdAndSizeId(Integer productId, Integer sizeId) {
        return productSizeRepository.findByProduct_IdAndSize_Id(productId, sizeId)
                .map(productSizeMapper::toDTO)
                .orElseThrow(() -> new AppException(ErrorCode.PRODUCT_SIZE_NOT_EXISTED));
    }

    @Override
    public int getStockByProductId(Integer productId) {
        List<ProductSizeEntity> list = productSizeRepository.findAllByProduct_Id(productId);
        return list.stream().mapToInt(ProductSizeEntity::getQuantity).sum();
    }
}
