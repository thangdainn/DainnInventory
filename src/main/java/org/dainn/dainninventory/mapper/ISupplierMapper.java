package org.dainn.dainninventory.mapper;

import org.dainn.dainninventory.dto.SupplierDTO;
import org.dainn.dainninventory.entity.SupplierEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface ISupplierMapper {
    SupplierEntity toEntity(SupplierDTO request);

    SupplierDTO toDTO(SupplierEntity entity);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdDate", ignore = true)
    @Mapping(target = "modifiedDate", ignore = true)
    SupplierEntity updateEntity(@MappingTarget SupplierEntity entity, SupplierDTO request);
}
