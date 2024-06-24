package org.dainn.dainninventory.mapper;

import org.dainn.dainninventory.controller.request.ProductRequest;
import org.dainn.dainninventory.dto.ProductDTO;
import org.dainn.dainninventory.entity.BrandEntity;
import org.dainn.dainninventory.entity.CategoryEntity;
import org.dainn.dainninventory.entity.ProductEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Named;

@Mapper(componentModel = "spring")
public interface IProductMapper {
    ProductEntity toEntity(ProductDTO request);
    @Mapping(target = "brandId", source = "brand", qualifiedByName = "toBrandDTO")
    @Mapping(target = "categoryId", source = "category", qualifiedByName = "toCategoryDTO")
    ProductDTO toDTO(ProductEntity entity);
    ProductDTO toDTO(ProductRequest request);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdDate", ignore = true)
    @Mapping(target = "modifiedDate", ignore = true)
    ProductEntity updateEntity(@MappingTarget ProductEntity entity, ProductDTO request);

    @Named("toCategoryDTO")
    default Integer toCategoryDTO(CategoryEntity category) {
        if (category != null) {
            return category.getId();
        }
        return null;
    }
    @Named("toBrandDTO")
    default Integer toBrandDTO(BrandEntity brand) {
        if (brand != null) {
            return brand.getId();
        }
        return null;
    }
}
