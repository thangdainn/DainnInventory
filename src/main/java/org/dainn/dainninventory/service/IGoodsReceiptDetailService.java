package org.dainn.dainninventory.service;

import org.dainn.dainninventory.dto.GoodsReceiptDetailDTO;

import java.util.List;

public interface IGoodsReceiptDetailService {
    void insert(List<GoodsReceiptDetailDTO> list, Integer goodReceiptId);
    List<GoodsReceiptDetailDTO> findByGoodReceiptId(Integer goodReceiptId);
    void deleteByGoodReceiptId(Integer goodReceiptId);
}
