package org.dainn.dainninventory.mapper;

import org.dainn.dainninventory.controller.request.RegisterRequest;
import org.dainn.dainninventory.controller.request.UserRequest;
import org.dainn.dainninventory.dto.UserDTO;
import org.dainn.dainninventory.entity.RoleEntity;
import org.dainn.dainninventory.entity.UserEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Named;

import java.util.ArrayList;
import java.util.List;

@Mapper(componentModel = "spring")
public interface IUserMapper {
    UserEntity toEntity(UserDTO request);
    UserRequest toUserRequest(RegisterRequest request);
    UserRequest toUserRequest(UserDTO dto);

    UserDTO toDTO(UserRequest userRequest);
    @Mapping(target = "rolesName", source = "roles", qualifiedByName = "toRoleDTO")
    UserDTO toDTO(UserEntity entity);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdDate", ignore = true)
    @Mapping(target = "modifiedDate", ignore = true)
    UserEntity updateEntity(@MappingTarget UserEntity entity, UserDTO request);

    @Named("toRoleDTO")
    default List<String> toRoleDTO(List<RoleEntity> roles) {
        if (roles == null) {
            return new ArrayList<>();
        }
        return roles.stream().map(RoleEntity::getName).toList();
    }

}
