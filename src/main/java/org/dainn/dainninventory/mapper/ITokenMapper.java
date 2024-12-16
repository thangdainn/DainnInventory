package org.dainn.dainninventory.mapper;

import org.dainn.dainninventory.dto.TokenDTO;
import org.dainn.dainninventory.entity.TokenEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ITokenMapper {
    TokenEntity toEntity(TokenDTO request);

    @Mapping(target = "userId", source = "user.id")
    TokenDTO toDTO(TokenEntity entity);
}
