package org.dainn.dainninventory.mapper;

import org.dainn.dainninventory.dto.GoodsReceiptDetailDTO;
import org.dainn.dainninventory.entity.GoodsReceiptDetailEntity;
import org.dainn.dainninventory.entity.ProductEntity;
import org.dainn.dainninventory.entity.SizeEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

@Mapper(componentModel = "spring")
public interface IGoodReceiptDetailMapper {
    GoodsReceiptDetailEntity toEntity(GoodsReceiptDetailDTO request);

    @Mapping(target = "productId", source = "product", qualifiedByName = "toProductDTO")
    @Mapping(target = "sizeId", source = "size", qualifiedByName = "toSizeDTO")
    GoodsReceiptDetailDTO toDTO(GoodsReceiptDetailEntity entity);

    @Named("toProductDTO")
    default Integer toProductDTO(ProductEntity product) {
        if (product != null) {
            return product.getId();
        }
        return null;
    }
    @Named("toSizeDTO")
    default Integer toSizeDTO(SizeEntity size) {
        if (size != null) {
            return size.getId();
        }
        return null;
    }
}
