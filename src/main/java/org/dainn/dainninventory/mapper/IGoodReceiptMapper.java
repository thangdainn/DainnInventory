package org.dainn.dainninventory.mapper;

import org.dainn.dainninventory.dto.GoodsReceiptDTO;
import org.dainn.dainninventory.dto.SupplierDTO;
import org.dainn.dainninventory.entity.GoodsReceiptEntity;
import org.dainn.dainninventory.entity.ProductEntity;
import org.dainn.dainninventory.entity.SupplierEntity;
import org.dainn.dainninventory.entity.UserEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Named;

@Mapper(componentModel = "spring")
public interface IGoodReceiptMapper {
    GoodsReceiptEntity toEntity(GoodsReceiptDTO request);

    @Mapping(target = "userId", source = "user", qualifiedByName = "toUserDTO")
    @Mapping(target = "supplierId", source = "supplier", qualifiedByName = "toSupplierDTO")
    GoodsReceiptDTO toDTO(GoodsReceiptEntity entity);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdDate", ignore = true)
    @Mapping(target = "modifiedDate", ignore = true)
    GoodsReceiptEntity updateEntity(@MappingTarget GoodsReceiptEntity entity, GoodsReceiptDTO request);

    @Named("toUserDTO")
    default Integer toUserDTO(UserEntity user) {
        if (user != null) {
            return user.getId();
        }
        return null;
    }
    @Named("toSupplierDTO")
    default Integer toSupplierDTO(SupplierEntity supplier) {
        if (supplier != null) {
            return supplier.getId();
        }
        return null;
    }
}
