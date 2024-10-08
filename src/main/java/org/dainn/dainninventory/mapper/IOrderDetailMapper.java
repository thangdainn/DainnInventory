package org.dainn.dainninventory.mapper;

import org.dainn.dainninventory.dto.OrderDetailDTO;
import org.dainn.dainninventory.entity.OrderDetailEntity;
import org.dainn.dainninventory.entity.ProductEntity;
import org.dainn.dainninventory.entity.SizeEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

@Mapper(componentModel = "spring")
public interface IOrderDetailMapper {
    OrderDetailEntity toEntity(OrderDetailDTO request);

    @Mapping(target = "productId", source = "product", qualifiedByName = "toProductDTO")
    @Mapping(target = "productName", source = "product", qualifiedByName = "toProductName")
    @Mapping(target = "productImage", source = "product", qualifiedByName = "toProductImage")
    @Mapping(target = "sizeId", source = "size", qualifiedByName = "toSizeDTO")
    OrderDetailDTO toDTO(OrderDetailEntity entity);

    @Named("toProductDTO")
    default Integer toProductDTO(ProductEntity product) {
        if (product != null) {
            return product.getId();
        }
        return null;
    }
    @Named("toProductName")
    default String toProductName(ProductEntity product) {
        if (product != null) {
            return product.getName();
        }
        return null;
    }
    @Named("toProductImage")
    default String toProductImage(ProductEntity product) {
        if (product != null) {
            return product.getImgUrl();
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
