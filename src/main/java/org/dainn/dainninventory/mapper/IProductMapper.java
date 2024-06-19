package org.dainn.dainninventory.mapper;

import org.dainn.dainninventory.controller.request.ProductRequest;
import org.dainn.dainninventory.dto.BrandDTO;
import org.dainn.dainninventory.dto.ProductDTO;
import org.dainn.dainninventory.entity.BrandEntity;
import org.dainn.dainninventory.entity.ProductEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface IProductMapper {
    ProductEntity toEntity(ProductDTO request);

    ProductDTO toDTO(ProductEntity entity);
    ProductDTO toDTO(ProductRequest request);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdDate", ignore = true)
    @Mapping(target = "modifiedDate", ignore = true)
    ProductEntity updateEntity(@MappingTarget ProductEntity entity, ProductDTO request);
}
