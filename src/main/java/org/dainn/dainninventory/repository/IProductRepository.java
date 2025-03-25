package org.dainn.dainninventory.repository;

import org.dainn.dainninventory.dto.product.ProductStats;
import org.dainn.dainninventory.entity.ProductEntity;
import org.dainn.dainninventory.utils.enums.OrderStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Repository
public interface IProductRepository extends JpaRepository<ProductEntity, Integer>, JpaSpecificationExecutor<ProductEntity> {
    boolean existsByName(String name);

    Optional<ProductEntity> findByCode(String code);

    @Modifying
    @Query("UPDATE ProductEntity r SET r.status = 0 WHERE r.id IN :ids")
    void deleteAllByIdInBatchCustom(@Param("ids") List<Integer> ids);

    List<ProductEntity> findAllByStatus(Integer status);

    @Query("SELECT new org.dainn.dainninventory.dto.product.ProductStats(p.id, p.name, p.image, SUM(od.quantity), SUM(od.quantity * od.price)) " +
            "FROM OrderDetailEntity od " +
            "JOIN od.product p " +
            "JOIN od.order o " +
            "WHERE o.status = :status AND o.orderDate BETWEEN :startDate AND :endDate " +
            "GROUP BY p.id, p.name, p.image " +
            "ORDER BY SUM(od.quantity * od.price) DESC " +
            "LIMIT 10")
    List<ProductStats> getTopProducts(@Param("startDate") Date startDate, @Param("endDate") Date endDate, @Param("status") OrderStatus status);

    @Query("SELECT p " +
            "FROM OrderDetailEntity od " +
            "JOIN od.product p " +
            "JOIN od.order o " +
            "WHERE o.orderDate BETWEEN :startDate AND :endDate " +
            "GROUP BY p.id " +
            "ORDER BY MAX(o.orderDate) DESC " +
            "LIMIT 10")
    List<ProductEntity> getRecentSale(Date startDate, Date endDate);
}
