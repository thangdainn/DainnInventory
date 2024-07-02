package org.dainn.dainninventory.service;

import org.dainn.dainninventory.dto.CartDTO;

import java.util.List;

public interface ICartService {
    CartDTO insert(CartDTO dto);
    CartDTO updateCart(CartDTO dto);
    void deleteCart(List<Integer> ids);
    void deleteAllCart(Integer userId);
    void checkout(Integer userId);
    List<CartDTO> findAllByUserId(Integer userId);
}
