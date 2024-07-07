package org.dainn.dainninventory.service.impl;

import com.fasterxml.jackson.core.type.TypeReference;
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
import org.dainn.dainninventory.service.IBaseRedisService;
import org.dainn.dainninventory.service.IGoodsReceiptDetailService;
import org.dainn.dainninventory.service.IGoodsReceiptService;
import org.dainn.dainninventory.utils.Paging;
import org.dainn.dainninventory.utils.constant.RedisConstant;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

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
    private final IBaseRedisService baseRedisService;

    @Transactional
    @Override
    public GoodsReceiptDTO insert(GoodsReceiptDTO dto) {
        GoodsReceiptEntity entity;
        if (dto.getId() != null) {
            GoodsReceiptEntity old = goodsReceiptRepository.findById(dto.getId())
                    .orElseThrow(() -> new AppException(ErrorCode.GOOD_RECEIPT_NOT_EXISTED));
            entity = goodReceiptMapper.updateEntity(old, dto);
        } else {
            entity = goodReceiptMapper.toEntity(dto);
        }
        entity = setConditions(entity, dto);
        return goodReceiptMapper.toDTO(entity);
    }

    @Transactional
    @Override
    public GoodsReceiptDTO update(GoodsReceiptDTO dto) {
        GoodsReceiptEntity old = goodsReceiptRepository.findById(dto.getId())
                .orElseThrow(() -> new AppException(ErrorCode.GOOD_RECEIPT_NOT_EXISTED));
        GoodsReceiptEntity entity = goodReceiptMapper.updateEntity(old, dto);
        entity = setConditions(entity, dto);
        baseRedisService.flushDb();
        return goodReceiptMapper.toDTO(entity);
    }

    private GoodsReceiptEntity setConditions(GoodsReceiptEntity entity, GoodsReceiptDTO dto){
        entity.setUser(userRepository.findById(dto.getUserId())
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED)));
        entity.setSupplier(supplierRepository.findById(dto.getSupplierId())
                .orElseThrow(() -> new AppException(ErrorCode.SUPPLIER_NOT_EXISTED)));
        entity = goodsReceiptRepository.save(entity);
        goodReceiptDetailService.insert(dto.getDetailDTOS(), entity.getId());
        return entity;
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
        String key = RedisConstant.GOODS_RECEIPT_KEY_PREFIX + "::id:" + id;
        GoodsReceiptDTO dto = baseRedisService.getCache(key, new TypeReference<GoodsReceiptDTO>() {});
        if (dto == null){
            dto = goodReceiptMapper.toDTO(goodsReceiptRepository.findById(id)
                    .orElseThrow(() -> new AppException(ErrorCode.GOOD_RECEIPT_NOT_EXISTED)));
            dto.setDetailDTOS(goodReceiptDetailService.findByGoodReceiptId(id));
            baseRedisService.setCache(key, dto);
        }
        return dto;
    }

    @Override
    public List<GoodsReceiptDTO> findAll() {
        String key = RedisConstant.GOODS_RECEIPTS_KEY_PREFIX;
        List<GoodsReceiptDTO> list = baseRedisService.getCache(key, new TypeReference<List<GoodsReceiptDTO>>() {});
        if (list == null){
            list = goodsReceiptRepository.findAll()
                    .stream().map(goodReceiptMapper::toDTO).toList();
            baseRedisService.setCache(key, list);
        }
        return list;
    }

    @Override
    public Page<GoodsReceiptDTO> findWithSpec(GoodsReceiptPageRequest request) {
        String key = RedisConstant.PRODUCTS_KEY_PREFIX + "::page:" + request.getPage() + "::size:" + request.getSize()
                + "::sort:" + request.getSortBy() + "::dir:" + request.getSortDir() + "::keyword:" + request.getKeyword()
                + "::fromDate:" + request.getFromDate() + "::toDate:" + request.getToDate() + "::status:" + request.getStatus();
        Page<GoodsReceiptDTO> pageDTO = baseRedisService.getCache(key, new TypeReference<Page<GoodsReceiptDTO>>() {});
        if (pageDTO != null){
            return pageDTO;
        }
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
        if (StringUtils.hasText(request.getKeyword())){
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
        pageDTO = page.map(goodReceiptMapper::toDTO);
        baseRedisService.setCache(key, pageDTO);
        return pageDTO;
    }
}
