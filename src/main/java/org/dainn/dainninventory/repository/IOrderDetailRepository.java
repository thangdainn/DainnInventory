package org.dainn.dainninventory.repository;

import org.dainn.dainninventory.entity.OrderDetailEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IOrderDetailRepository extends JpaRepository<OrderDetailEntity, Integer> {
    List<OrderDetailEntity> findByOrder_Id(Integer orderId);
}
