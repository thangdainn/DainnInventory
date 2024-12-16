package org.dainn.dainninventory.mapper;

import org.dainn.dainninventory.dto.ProductSizeDTO;
import org.dainn.dainninventory.entity.ProductSizeEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface IProductSizeMapper {
    ProductSizeEntity toEntity(ProductSizeDTO request);

    @Mapping(target = "productId", source = "product.id")
    @Mapping(target = "sizeId", source = "size.id")
    ProductSizeDTO toDTO(ProductSizeEntity entity);
}
