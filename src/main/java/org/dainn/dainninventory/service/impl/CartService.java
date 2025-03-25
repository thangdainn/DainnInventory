package org.dainn.dainninventory.service.impl;

import lombok.RequiredArgsConstructor;
import org.dainn.dainninventory.dto.cart.CartPageRequest;
import org.dainn.dainninventory.dto.cart.CartDTO;
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
import org.dainn.dainninventory.service.IProductSizeService;
import org.dainn.dainninventory.service.ISizeService;
import org.dainn.dainninventory.utils.Paging;
import org.springframework.data.domain.Page;
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
    private final IProductSizeService productSizeService;
    private final NotificationService notificationService;

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
//        notificationService.sendNotification(
//                dto.getUserId().toString(),
//                Notification.builder()
//                        .status(NotificationStatus.SUCCESS)
//                        .title("Test")
//                        .message("ccccccccccccccccc")
//                        .build()
//        );
        return cartMapper.toDTO(cartRepository.save(entity));
    }

    @Override
    public List<CartDTO> inserts(List<CartDTO> dtos) {
        return dtos.stream().map(this::insert).toList();
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
    public Page<CartDTO> findAllByUserId(CartPageRequest request) {
        return cartRepository.findAllByUserId(request.getUserId(), Paging.getPageable(request))
                .map((entity) -> {
                    CartDTO dto = cartMapper.toDTO(entity);
                    dto.setProduct(productService.findById(dto.getProductId()));
                    dto.setSize(sizeService.findById(dto.getSizeId()));
                    dto.setStock(productSizeService.findByProductIdAndSizeId(dto.getProductId(), dto.getSizeId()).getQuantity());
                    return dto;
                });
    }

    @Override
    public int countByUserId(Integer userId) {
        return cartRepository.countByUserId(userId);
    }
}
