package org.dainn.dainninventory.mapper;

import org.dainn.dainninventory.dto.GoodsReceiptDetailDTO;
import org.dainn.dainninventory.entity.GoodsReceiptDetailEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface IGoodReceiptDetailMapper {
    GoodsReceiptDetailEntity toEntity(GoodsReceiptDetailDTO request);

    @Mapping(target = "productId", source = "product.id")
    @Mapping(target = "sizeId", source = "size.id")
    GoodsReceiptDetailDTO toDTO(GoodsReceiptDetailEntity entity);
}
