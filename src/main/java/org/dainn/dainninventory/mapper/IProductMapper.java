package org.dainn.dainninventory.mapper;

import org.dainn.dainninventory.dto.product.ProductRequest;
import org.dainn.dainninventory.dto.product.ProductDTO;
import org.dainn.dainninventory.entity.ProductEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface IProductMapper {
    ProductEntity toEntity(ProductDTO request);
    @Mapping(target = "brandId", source = "brand.id")
    @Mapping(target = "brandName", source = "brand.name")
    @Mapping(target = "categoryId", source = "category.id")
    @Mapping(target = "categoryName", source = "category.name")
    ProductDTO toDTO(ProductEntity entity);
    ProductDTO toDTO(ProductRequest request);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdDate", ignore = true)
    @Mapping(target = "modifiedDate", ignore = true)
    ProductEntity updateEntity(@MappingTarget ProductEntity entity, ProductDTO request);
}
