package org.dainn.dainninventory.mapper;

import org.dainn.dainninventory.dto.BrandDTO;
import org.dainn.dainninventory.dto.RoleDTO;
import org.dainn.dainninventory.entity.BrandEntity;
import org.dainn.dainninventory.entity.RoleEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface IBrandMapper {
    BrandEntity toEntity(BrandDTO request);

    BrandDTO toDTO(BrandEntity entity);

//    @Mapping(target = "name", source = "request.name")

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdDate", ignore = true)
    @Mapping(target = "modifiedDate", ignore = true)
    BrandEntity updateEntity(@MappingTarget BrandEntity entity, BrandDTO request);
}
