package org.dainn.dainninventory.service;

import org.dainn.dainninventory.dto.GoodsReceiptDTO;

import java.util.List;

public interface IGoodsReceiptService {
    GoodsReceiptDTO save(GoodsReceiptDTO dto);
    void delete(List<Integer> ids);
    GoodsReceiptDTO findById(Integer id);
    List<GoodsReceiptDTO> findAll();
}
