package org.dainn.dainninventory.repository;

import org.dainn.dainninventory.entity.OrderDetailEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface IOrderDetailRepository extends JpaRepository<OrderDetailEntity, Integer> {
    List<OrderDetailEntity> findByOrder_Id(Integer orderId);

//    @Query("SELECT od FROM OrderDetailEntity od JOIN OrderEntity o ON o.id = od.order.id WHERE o.orderDate BETWEEN :startDate AND :endDate")
    List<OrderDetailEntity> findAllByOrder_OrderDateBetween(Date startDate, Date endDate);
}
