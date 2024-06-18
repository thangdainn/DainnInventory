package org.dainn.dainninventory.mapper;

import org.dainn.dainninventory.dto.BrandDTO;
import org.dainn.dainninventory.dto.CategoryDTO;
import org.dainn.dainninventory.entity.BrandEntity;
import org.dainn.dainninventory.entity.CategoryEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface ICategoryMapper {
    CategoryEntity toEntity(CategoryDTO request);

    CategoryDTO toDTO(CategoryEntity entity);

//    @Mapping(target = "name", source = "request.name")

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdDate", ignore = true)
    @Mapping(target = "modifiedDate", ignore = true)
    CategoryEntity updateEntity(@MappingTarget CategoryEntity entity, CategoryDTO request);
}
