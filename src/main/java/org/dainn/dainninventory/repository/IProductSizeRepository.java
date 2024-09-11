package org.dainn.dainninventory.repository;

import org.dainn.dainninventory.entity.OrderDetailEntity;
import org.dainn.dainninventory.entity.ProductSizeEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface IProductSizeRepository extends JpaRepository<ProductSizeEntity, Integer> {
    //    List<ProductSizeEntity> findByOrder_Id(Integer orderId);
    Optional<ProductSizeEntity> findByProduct_IdAndSize_Id(Integer productId, Integer sizeId);
    @Modifying
    @Query("UPDATE ProductSizeEntity r SET r.quantity = :quantity WHERE r.product.id = :productId AND r.size.id = :sizeId")
    void updateQuantityByProduct_IdAndSize_Id(@Param("productId") Integer productId,
                                    @Param("quantity") Integer quantity,
                                    @Param("sizeId") Integer sizeId);

    List<ProductSizeEntity> findAllByProduct_Code(String code);
}
