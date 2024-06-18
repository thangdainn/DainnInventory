package org.dainn.dainninventory.mapper;

import org.dainn.dainninventory.dto.ImageDTO;
import org.dainn.dainninventory.dto.ProductDTO;
import org.dainn.dainninventory.entity.ImageEntity;
import org.dainn.dainninventory.entity.ProductEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface IImageMapper {
    ImageEntity toEntity(ImageDTO dto);

    ImageDTO toDTO(ImageEntity entity);

}
