package org.dainn.dainninventory.mapper;

import org.dainn.dainninventory.dto.order.OrderDetailDTO;
import org.dainn.dainninventory.entity.OrderDetailEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface IOrderDetailMapper {
    OrderDetailEntity toEntity(OrderDetailDTO request);

    @Mapping(target = "productId", source = "product.id")
    @Mapping(target = "productCode", source = "product.code")
    @Mapping(target = "productName", source = "product.name")
    @Mapping(target = "productImage", source = "product.image")
    @Mapping(target = "sizeId", source = "size.id")
    @Mapping(target = "sizeName", source = "size.name")

    OrderDetailDTO toDTO(OrderDetailEntity entity);
}