package org.dainn.dainninventory.service.impl;

import lombok.RequiredArgsConstructor;
import org.dainn.dainninventory.dto.GoodsReceiptDTO;
import org.dainn.dainninventory.entity.GoodsReceiptEntity;
import org.dainn.dainninventory.exception.AppException;
import org.dainn.dainninventory.exception.ErrorCode;
import org.dainn.dainninventory.mapper.IGoodReceiptMapper;
import org.dainn.dainninventory.repository.IGoodsReceiptRepository;
import org.dainn.dainninventory.repository.ISupplierRepository;
import org.dainn.dainninventory.repository.IUserRepository;
import org.dainn.dainninventory.service.IGoodsReceiptDetailService;
import org.dainn.dainninventory.service.IGoodsReceiptService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class GoodsReceiptService implements IGoodsReceiptService {
    private final IGoodsReceiptRepository goodsReceiptRepository;
    private final IUserRepository userRepository;
    private final ISupplierRepository supplierRepository;
    private final IGoodsReceiptDetailService goodReceiptDetailService;
    private final IGoodReceiptMapper goodReceiptMapper;

    @Transactional
    @Override
    public GoodsReceiptDTO save(GoodsReceiptDTO dto) {
        GoodsReceiptEntity entity;
        if (dto.getId() != null) {
            GoodsReceiptEntity old = goodsReceiptRepository.findById(dto.getId())
                    .orElseThrow(() -> new AppException(ErrorCode.GOOD_RECEIPT_NOT_EXISTED));
            entity = goodReceiptMapper.updateEntity(old, dto);
        } else {
            entity = goodReceiptMapper.toEntity(dto);
        }
        entity.setUser(userRepository.findById(dto.getUserId())
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED)));
        entity.setSupplier(supplierRepository.findById(dto.getSupplierId())
                .orElseThrow(() -> new AppException(ErrorCode.SUPPLIER_NOT_EXISTED)));
        entity = goodsReceiptRepository.save(entity);
        goodReceiptDetailService.insert(dto.getDetailDTOS(), entity.getId());
        return goodReceiptMapper.toDTO(entity);
    }

    @Transactional
    @Override
    public void delete(List<Integer> ids) {
        goodsReceiptRepository.deleteAllByIdInBatchCustom(ids);
        for (Integer id : ids) {
            goodReceiptDetailService.deleteByGoodReceiptId(id);
        }
    }

    @Override
    public GoodsReceiptDTO findById(Integer id) {
        GoodsReceiptDTO dto = goodReceiptMapper.toDTO(goodsReceiptRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.GOOD_RECEIPT_NOT_EXISTED)));
        dto.setDetailDTOS(goodReceiptDetailService.findByGoodReceiptId(id));
        return dto;
    }


    @Override
    public List<GoodsReceiptDTO> findAll() {
        return goodsReceiptRepository.findAll()
                .stream().map(goodReceiptMapper::toDTO).toList();
    }
}
