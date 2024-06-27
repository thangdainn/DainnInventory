package org.dainn.dainninventory.service;

import org.dainn.dainninventory.controller.request.GoodsReceiptPageRequest;
import org.dainn.dainninventory.dto.GoodsReceiptDTO;
import org.springframework.data.domain.Page;

import java.util.List;

public interface IGoodsReceiptService {
    GoodsReceiptDTO save(GoodsReceiptDTO dto);
    void delete(List<Integer> ids);
    GoodsReceiptDTO findById(Integer id);
    List<GoodsReceiptDTO> findAll();

    Page<GoodsReceiptDTO> findWithSpec(GoodsReceiptPageRequest request);
}
