package org.dainn.dainninventory.mapper;

import org.dainn.dainninventory.controller.request.ProductRequest;
import org.dainn.dainninventory.dto.InventoryDTO;
import org.dainn.dainninventory.entity.InventoryEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface IInventoryMapper {
    InventoryEntity toEntity(InventoryDTO dto);
    InventoryDTO toDTO(InventoryEntity entity);
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "status", ignore = true)
    @Mapping(target = "createdDate", ignore = true)
    @Mapping(target = "modifiedDate", ignore = true)
    InventoryDTO toDTO(ProductRequest request);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdDate", ignore = true)
    @Mapping(target = "modifiedDate", ignore = true)
    InventoryEntity updateEntity(@MappingTarget InventoryEntity entity, InventoryDTO request);
}
