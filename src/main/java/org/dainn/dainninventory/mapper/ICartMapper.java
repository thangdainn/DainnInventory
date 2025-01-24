package org.dainn.dainninventory.mapper;

import org.dainn.dainninventory.dto.CartDTO;
import org.dainn.dainninventory.entity.CartEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface ICartMapper {
    CartEntity toEntity(CartDTO request);

    @Mapping(target = "productId", source = "product.id")
    @Mapping(target = "userId", source = "user.id")
    @Mapping(target = "sizeId", source = "size.id")
    CartDTO toDTO(CartEntity entity);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdDate", ignore = true)
    @Mapping(target = "modifiedDate", ignore = true)
    CartEntity updateEntity(@MappingTarget CartEntity entity, CartDTO request);
}
