package org.dainn.dainninventory.mapper;

import org.dainn.dainninventory.dto.OrderDTO;
import org.dainn.dainninventory.entity.OrderEntity;
import org.dainn.dainninventory.entity.UserEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Named;

@Mapper(componentModel = "spring")
public interface IOrderMapper {
    OrderEntity toEntity(OrderDTO request);

    @Mapping(target = "userId", source = "user", qualifiedByName = "toUserDTO")
    OrderDTO toDTO(OrderEntity entity);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "orderDate", ignore = true)
    @Mapping(target = "modifiedDate", ignore = true)
    OrderEntity updateEntity(@MappingTarget OrderEntity entity, OrderDTO request);

    @Named("toUserDTO")
    default Integer toUserDTO(UserEntity user) {
        if (user != null) {
            return user.getId();
        }
        return null;
    }
}
