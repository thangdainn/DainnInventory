package org.dainn.dainninventory.repository;

import org.dainn.dainninventory.entity.GoodsReceiptEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository

public interface IGoodsReceiptRepository extends JpaRepository<GoodsReceiptEntity, Integer>, JpaSpecificationExecutor<GoodsReceiptEntity> {
    @Modifying
    @Query("UPDATE GoodsReceiptEntity r SET r.status = 0 WHERE r.id IN :ids")
    void deleteAllByIdInBatchCustom(@Param("ids") List<Integer> ids);
}
