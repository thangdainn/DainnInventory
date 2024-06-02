package org.dainn.dainninventory.mapper;

import org.dainn.dainninventory.dto.RoleDTO;
import org.dainn.dainninventory.entity.RoleEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface RoleMapper {
    RoleEntity toEntity(RoleDTO request);
}
