package org.dainn.dainninventory.repository;

import org.dainn.dainninventory.entity.ProductEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface IProductRepository extends JpaRepository<ProductEntity, Integer> {

    boolean existsByCode(String code);
    boolean existsByName(String name);
    Optional<ProductEntity> findByCode(String code);
    @Modifying
    @Query("UPDATE ProductEntity r SET r.status = 0 WHERE r.id IN :ids")
    void deleteAllByIdInBatchCustom(@Param("ids") List<Integer> ids);

    @Query("SELECT p FROM ProductEntity p INNER JOIN BrandEntity b ON p.brand.id = b.id INNER JOIN CategoryEntity c ON p.category.id = c.id" +
             " WHERE (:keyword IS NULL OR p.name LIKE %:keyword%) AND (:brand IS NULL OR p.id = :brand) AND (:category IS NULL OR c.id = :category) AND (:status IS NULL OR p.status = :status)")
    Page<ProductEntity> findByDynamicFilters(@Param("keyword") String keyword, @Param("brand") Integer brandId, @Param("category") Integer categoryId, @Param("status") Integer status, Pageable pageable);
}
