package org.dainn.dainninventory.service;

import org.dainn.dainninventory.controller.request.CartPageRequest;
import org.dainn.dainninventory.dto.CartDTO;
import org.springframework.data.domain.Page;

import java.util.List;

public interface ICartService {
    CartDTO insert(CartDTO dto);
    List<CartDTO> inserts(List<CartDTO> dtos);
    CartDTO updateCart(CartDTO dto);
    void deleteCart(List<Integer> ids);
    void deleteAllCart(Integer userId);
    Page<CartDTO> findAllByUserId(CartPageRequest request);
    int countByUserId(Integer userId);
}
