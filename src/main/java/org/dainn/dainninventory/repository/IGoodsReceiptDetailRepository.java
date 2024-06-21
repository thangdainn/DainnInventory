package org.dainn.dainninventory.repository;

import org.dainn.dainninventory.entity.BrandEntity;
import org.dainn.dainninventory.entity.GoodsReceiptDetailEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository

public interface IGoodsReceiptDetailRepository extends JpaRepository<GoodsReceiptDetailEntity, Integer> {
    List<GoodsReceiptDetailEntity> findByGoodsReceipt_Id(Integer goodsReceiptId);
}
