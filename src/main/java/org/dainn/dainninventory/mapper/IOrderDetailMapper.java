package org.dainn.dainninventory.mapper;

import org.dainn.dainninventory.dto.OrderDetailDTO;
import org.dainn.dainninventory.entity.OrderDetailEntity;
import org.dainn.dainninventory.entity.ProductEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

@Mapper(componentModel = "spring")
public interface IOrderDetailMapper {
    OrderDetailEntity toEntity(OrderDetailDTO request);

    @Mapping(target = "productId", source = "product", qualifiedByName = "toProductDTO")
    OrderDetailDTO toDTO(OrderDetailEntity entity);

    @Named("toProductDTO")
    default Integer toProductDTO(ProductEntity product) {
        if (product != null) {
            return product.getId();
        }
        return null;
    }
}
