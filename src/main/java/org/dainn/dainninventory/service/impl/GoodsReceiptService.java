package org.dainn.dainninventory.service.impl;

import lombok.RequiredArgsConstructor;
import org.dainn.dainninventory.controller.request.GoodsReceiptPageRequest;
import org.dainn.dainninventory.dto.GoodsReceiptDTO;
import org.dainn.dainninventory.entity.GoodsReceiptEntity;
import org.dainn.dainninventory.exception.AppException;
import org.dainn.dainninventory.exception.ErrorCode;
import org.dainn.dainninventory.mapper.IGoodReceiptMapper;
import org.dainn.dainninventory.repository.IGoodsReceiptRepository;
import org.dainn.dainninventory.repository.ISupplierRepository;
import org.dainn.dainninventory.repository.IUserRepository;
import org.dainn.dainninventory.repository.specification.SearchOperation;
import org.dainn.dainninventory.repository.specification.SpecSearchCriteria;
import org.dainn.dainninventory.repository.specification.SpecificationBuilder;
import org.dainn.dainninventory.service.IGoodsReceiptDetailService;
import org.dainn.dainninventory.service.IGoodsReceiptService;
import org.dainn.dainninventory.utils.Paging;
import org.dainn.dainninventory.utils.ValidateString;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

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

    @Override
    public Page<GoodsReceiptDTO> findWithSpec(GoodsReceiptPageRequest request) {
        SpecificationBuilder<GoodsReceiptEntity> builder = new SpecificationBuilder<>();
        Page<GoodsReceiptEntity> page;
        Specification<GoodsReceiptEntity> spec;
        if (request.getFromDate() != null){
            builder.with("createdDate", SearchOperation.GREATER_THAN_OR_EQUAL, request.getFromDate(), false);
        }
        if (request.getToDate() != null){
            builder.with("createdDate", SearchOperation.LESS_THAN_OR_EQUAL, request.getToDate(), false);
        }
        builder.with("status", SearchOperation.EQUALITY, request.getStatus(), false);
        if (!ValidateString.isNullOrBlank(request.getKeyword())){
            try {
                builder.with("id", SearchOperation.EQUALITY, Integer.valueOf(request.getKeyword()), false);
                spec = builder.build();
            } catch (NumberFormatException e){
                List<SpecSearchCriteria> userCriteria = new ArrayList<>();
                userCriteria.add(new SpecSearchCriteria("name", SearchOperation.CONTAINS, request.getKeyword(), true));
                userCriteria.add(new SpecSearchCriteria("email", SearchOperation.CONTAINS, request.getKeyword(), true));
                Specification<GoodsReceiptEntity> userSpec = builder.joinTableWithCondition("user", userCriteria);
                List<SpecSearchCriteria> supplierCriteria = new ArrayList<>();
                supplierCriteria.add(new SpecSearchCriteria("name", SearchOperation.CONTAINS, request.getKeyword(), true));
                Specification<GoodsReceiptEntity> supplierSpec = builder.joinTableWithCondition("supplier", supplierCriteria);
                spec = Specification.where(builder.build()).and(Specification.where(userSpec).or(supplierSpec));
            }
        } else {
            spec = builder.build();
        }
        page = goodsReceiptRepository.findAll(Objects.requireNonNull(spec), Paging.getPageable(request));
        return page.map(goodReceiptMapper::toDTO);
    }
}
