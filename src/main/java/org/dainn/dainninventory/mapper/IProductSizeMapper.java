package org.dainn.dainninventory.mapper;

import org.dainn.dainninventory.dto.product.ProductSizeDTO;
import org.dainn.dainninventory.entity.ProductSizeEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface IProductSizeMapper {
    ProductSizeEntity toEntity(ProductSizeDTO request);

    @Mapping(target = "productId", source = "product.id")
    @Mapping(target = "sizeId", source = "size.id")
    @Mapping(target = "sizeName", source = "size.name")
    ProductSizeDTO toDTO(ProductSizeEntity entity);
}
