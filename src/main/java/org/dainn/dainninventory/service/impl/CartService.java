package org.dainn.dainninventory.service.impl;

import lombok.RequiredArgsConstructor;
import org.dainn.dainninventory.dto.CartDTO;
import org.dainn.dainninventory.entity.CartEntity;
import org.dainn.dainninventory.exception.AppException;
import org.dainn.dainninventory.exception.ErrorCode;
import org.dainn.dainninventory.mapper.ICartMapper;
import org.dainn.dainninventory.repository.ICartRepository;
import org.dainn.dainninventory.repository.IProductRepository;
import org.dainn.dainninventory.repository.ISizeRepository;
import org.dainn.dainninventory.repository.IUserRepository;
import org.dainn.dainninventory.service.ICartService;
import org.dainn.dainninventory.service.IProductService;
import org.dainn.dainninventory.service.ISizeService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CartService implements ICartService {
    private final ICartRepository cartRepository;
    private final ICartMapper cartMapper;
    private final IProductRepository productRepository;
    private final ISizeRepository sizeRepository;
    private final IUserRepository userRepository;
    private final IProductService productService;
    private final ISizeService sizeService;

    @Transactional
    @Override
    public CartDTO insert(CartDTO dto) {
        CartEntity entity = cartMapper.toEntity(dto);
        Optional<CartEntity> optional =
                cartRepository.findByUserIdAndProductIdAndSizeId(dto.getUserId(), dto.getProductId(), dto.getSizeId());
        if (optional.isPresent()) {
            entity = optional.get();
            entity.setQuantity(entity.getQuantity() + dto.getQuantity());
        } else {
            entity.setProduct(productRepository.findById(dto.getProductId())
                    .orElseThrow(() -> new AppException(ErrorCode.PRODUCT_NOT_EXISTED)));
            entity.setUser(userRepository.findById(dto.getUserId())
                    .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED)));
            entity.setSize(sizeRepository.findById(dto.getSizeId())
                    .orElseThrow(() -> new AppException(ErrorCode.SIZE_NOT_EXISTED)));
        }
        return cartMapper.toDTO(cartRepository.save(entity));
    }

    @Transactional
    @Override
    public CartDTO updateCart(CartDTO dto) {
        CartEntity entity = cartRepository.findById(dto.getId())
                .orElseThrow(() -> new AppException(ErrorCode.CART_ITEM_NOT_EXISTED));
        entity = cartMapper.updateEntity(entity, dto);
        return cartMapper.toDTO(cartRepository.save(entity));
    }

    @Transactional
    @Override
    public void deleteCart(List<Integer> ids) {
        cartRepository.deleteAllByIdInBatch(ids);
    }

    @Transactional
    @Override
    public void deleteAllCart(Integer userId) {
        cartRepository.deleteAllByUserId(userId);
    }

    @Override
    public void checkout(Integer userId) {

    }

    @Override
    public List<CartDTO> findAllByUserId(Integer userId) {
        return cartRepository.findAllByUserId(userId)
                .stream().map((entity) -> {
                    CartDTO dto = cartMapper.toDTO(entity);
                    dto.setProduct(productService.findById(dto.getProductId()));
                    dto.setSize(sizeService.findById(dto.getSizeId()));
                    return dto;
                }).toList();
    }
}
