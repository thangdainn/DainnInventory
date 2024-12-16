package org.dainn.dainninventory.mapper;

import org.dainn.dainninventory.dto.Order.MyOrderDTO;
import org.dainn.dainninventory.dto.OrderDTO;
import org.dainn.dainninventory.entity.OrderEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface IOrderMapper {
    OrderEntity toEntity(OrderDTO request);

    @Mapping(target = "userId", source = "user.id")
    OrderDTO toDTO(OrderEntity entity);

    MyOrderDTO toMyOrderDTO(OrderEntity entity);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "orderDate", ignore = true)
    @Mapping(target = "modifiedDate", ignore = true)
    OrderEntity updateEntity(@MappingTarget OrderEntity entity, OrderDTO request);
}
