package org.dainn.dainninventory.mapper;

import org.dainn.dainninventory.dto.CartDTO;
import org.dainn.dainninventory.entity.CartEntity;
import org.dainn.dainninventory.entity.ProductEntity;
import org.dainn.dainninventory.entity.UserEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Named;

@Mapper(componentModel = "spring")
public interface ICartMapper {
    CartEntity toEntity(CartDTO request);

    @Mapping(target = "productId", source = "product", qualifiedByName = "toProductDTO")
    @Mapping(target = "userId", source = "user", qualifiedByName = "toUserDTO")
    CartDTO toDTO(CartEntity entity);

    @Mapping(target = "id", ignore = true)
    CartEntity updateEntity(@MappingTarget CartEntity entity, CartDTO request);

    @Named("toProductDTO")
    default Integer toProductDTO(ProductEntity product) {
        if (product != null) {
            return product.getId();
        }
        return null;
    }
    @Named("toUserDTO")
    default Integer toUserDTO(UserEntity user) {
        if (user != null) {
            return user.getId();
        }
        return null;
    }
}
