package org.dainn.dainninventory.mapper;

import org.dainn.dainninventory.dto.user.UserRequest;
import org.dainn.dainninventory.dto.user.UserDTO;
import org.dainn.dainninventory.dto.auth.RegisterDTO;
import org.dainn.dainninventory.entity.UserEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface IUserMapper {
    UserEntity toEntity(UserDTO request);
    UserRequest toUserRequest(RegisterDTO request);
    UserRequest toUserRequest(UserDTO dto);

    UserDTO toDTO(UserRequest userRequest);
    @Mapping(target = "roleName", source = "role.name")
    UserDTO toDTO(UserEntity entity);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdDate", ignore = true)
    @Mapping(target = "modifiedDate", ignore = true)
    UserEntity updateEntity(@MappingTarget UserEntity entity, UserDTO request);
}
