package org.dainn.dainninventory.mapper;

import org.dainn.dainninventory.dto.RoleDTO;
import org.dainn.dainninventory.entity.RoleEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface IRoleMapper {
    RoleEntity toEntity(RoleDTO request);

    RoleDTO toDTO(RoleEntity entity);

//    @Mapping(target = "name", source = "request.name")

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdDate", ignore = true)
    @Mapping(target = "modifiedDate", ignore = true)
    RoleEntity updateEntity(@MappingTarget RoleEntity entity, RoleDTO request);
}
