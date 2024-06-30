package org.dainn.dainninventory.mapper;

import org.dainn.dainninventory.dto.ImageDTO;
import org.dainn.dainninventory.entity.ImageEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface IImageMapper {
    ImageEntity toEntity(ImageDTO dto);

    ImageDTO toDTO(ImageEntity entity);

}
