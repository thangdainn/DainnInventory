package org.dainn.dainninventory.repository;

import org.dainn.dainninventory.entity.BrandEntity;
import org.dainn.dainninventory.entity.OrderEntity;
import org.dainn.dainninventory.utils.OrderStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface IOrderRepository extends JpaRepository<OrderEntity, Integer> {
    @Modifying
    @Query("UPDATE OrderEntity o SET o.status = :status WHERE o.id = :id")
    int updateStatus(@Param("id") Integer id, @Param("status") OrderStatus status);
}
