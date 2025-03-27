package org.dainn.dainninventory.repository;

import org.dainn.dainninventory.dto.statistic.SaleByCateDTO;
import org.dainn.dainninventory.entity.OrderEntity;
import org.dainn.dainninventory.utils.enums.OrderStatus;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface IOrderRepository extends JpaRepository<OrderEntity, Integer>, JpaSpecificationExecutor<OrderEntity> {
    @Modifying
    @Query("UPDATE OrderEntity o SET o.status = :status WHERE o.id IN :ids")
    void updateStatuses(@Param("ids") List<Integer> ids, @Param("status") OrderStatus status);

    @Modifying
    @Query("UPDATE OrderEntity o SET o.isPaid = :isPaid, o.status = :status  WHERE o.id = :id")
    void updatePaid(@Param("id") Integer id, @Param("isPaid") boolean isPaid, @Param("status") OrderStatus status);

    List<OrderEntity> findAllByOrderDateBetween(Date startDate, Date endDate);
    List<OrderEntity> findAllByStatusAndOrderDateBetween(OrderStatus status, Date startDate, Date endDate, Sort sort);

    @Query("SELECT new org.dainn.dainninventory.dto.statistic.SaleByCateDTO(c.name, SUM(od.quantity), SUM(o.totalAmount)) " +
            "FROM OrderDetailEntity od " +
            "JOIN od.product p " +
            "JOIN p.category c " +
            "JOIN od.order o " +
            "WHERE o.status = :status AND o.orderDate BETWEEN :startDate AND :endDate " +
            "GROUP BY c.name")
    List<SaleByCateDTO> getSalesByCategory(Date startDate, Date endDate, OrderStatus status);

    @Query("SELECT o " +
            "FROM OrderEntity o " +
            "JOIN o.orderDetails od " +
            "JOIN od.product p " +
            "WHERE p.id = :productId AND o.orderDate BETWEEN :startDate AND :endDate")
    List<OrderEntity> findAllByProductId(Integer productId, Date startDate, Date endDate, Sort sort);
}
