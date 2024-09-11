package org.dainn.dainninventory.mapper;

import org.dainn.dainninventory.dto.OrderDetailDTO;
import org.dainn.dainninventory.dto.ProductSizeDTO;
import org.dainn.dainninventory.entity.OrderDetailEntity;
import org.dainn.dainninventory.entity.ProductEntity;
import org.dainn.dainninventory.entity.ProductSizeEntity;
import org.dainn.dainninventory.entity.SizeEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

@Mapper(componentModel = "spring")
public interface IProductSizeMapper {
    ProductSizeEntity toEntity(ProductSizeDTO request);

    @Mapping(target = "productId", source = "product", qualifiedByName = "toProductDTO")
    @Mapping(target = "sizeId", source = "size", qualifiedByName = "toSizeDTO")
    ProductSizeDTO toDTO(ProductSizeEntity entity);

    @Named("toProductDTO")
    default Integer toProductDTO(ProductEntity product) {
        if (product != null) {
            return product.getId();
        }
        return null;
    }

    @Named("toSizeDTO")
    default Integer toSizeDTO(SizeEntity size) {
        if (size != null) {
            return size.getId();
        }
        return null;
    }
}
