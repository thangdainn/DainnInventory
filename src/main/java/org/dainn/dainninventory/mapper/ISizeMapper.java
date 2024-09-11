package org.dainn.dainninventory.mapper;

import org.dainn.dainninventory.dto.BrandDTO;
import org.dainn.dainninventory.dto.SizeDTO;
import org.dainn.dainninventory.entity.BrandEntity;
import org.dainn.dainninventory.entity.SizeEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface ISizeMapper {
    SizeEntity toEntity(SizeDTO request);

    SizeDTO toDTO(SizeEntity entity);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdDate", ignore = true)
    @Mapping(target = "modifiedDate", ignore = true)
    SizeEntity updateEntity(@MappingTarget SizeEntity entity, SizeDTO request);
}
