package org.dainn.dainninventory.mapper;

import org.dainn.dainninventory.dto.GoodsReceiptDTO;
import org.dainn.dainninventory.entity.GoodsReceiptEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface IGoodReceiptMapper {
    GoodsReceiptEntity toEntity(GoodsReceiptDTO request);

    @Mapping(target = "userId", source = "user.id")
    @Mapping(target = "supplierId", source = "supplier.id")
    GoodsReceiptDTO toDTO(GoodsReceiptEntity entity);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdDate", ignore = true)
    @Mapping(target = "modifiedDate", ignore = true)
    GoodsReceiptEntity updateEntity(@MappingTarget GoodsReceiptEntity entity, GoodsReceiptDTO request);
}
