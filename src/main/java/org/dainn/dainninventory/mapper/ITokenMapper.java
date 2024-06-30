package org.dainn.dainninventory.mapper;

import org.dainn.dainninventory.dto.TokenDTO;
import org.dainn.dainninventory.entity.TokenEntity;
import org.dainn.dainninventory.entity.UserEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

@Mapper(componentModel = "spring")
public interface ITokenMapper {
    TokenEntity toEntity(TokenDTO request);

    @Mapping(target = "userId", source = "user", qualifiedByName = "toUserDTO")
    TokenDTO toDTO(TokenEntity entity);

    @Named("toUserDTO")
    default Integer toUserDTO(UserEntity user) {
        if (user != null) {
            return user.getId();
        }
        return null;
    }
}
