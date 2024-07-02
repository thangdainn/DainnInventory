package org.dainn.dainninventory.service.impl;

import lombok.RequiredArgsConstructor;
import org.dainn.dainninventory.dto.CartDTO;
import org.dainn.dainninventory.entity.CartEntity;
import org.dainn.dainninventory.exception.AppException;
import org.dainn.dainninventory.exception.ErrorCode;
import org.dainn.dainninventory.mapper.ICartMapper;
import org.dainn.dainninventory.repository.ICartRepository;
import org.dainn.dainninventory.repository.IProductRepository;
import org.dainn.dainninventory.repository.IUserRepository;
import org.dainn.dainninventory.service.ICartService;
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
    private final IUserRepository userRepository;

    @Transactional
    @Override
    public CartDTO insert(CartDTO dto) {
        CartEntity entity = cartMapper.toEntity(dto);
        Optional<CartEntity> optional = cartRepository.findByUserIdAndProductId(dto.getUserId(), dto.getProductId());
        if (optional.isPresent()) {
            entity = optional.get();
            entity.setQuantity(entity.getQuantity() + dto.getQuantity());
        } else {
            entity.setProduct(productRepository.findById(dto.getProductId())
                    .orElseThrow(() -> new AppException(ErrorCode.PRODUCT_NOT_EXISTED)));
            entity.setUser(userRepository.findById(dto.getUserId())
                    .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED)));
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
                .stream().map(cartMapper::toDTO).toList();
    }
}
