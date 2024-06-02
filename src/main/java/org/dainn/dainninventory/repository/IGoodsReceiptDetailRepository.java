package org.dainn.dainninventory.repository;

import org.dainn.dainninventory.entity.BrandEntity;
import org.dainn.dainninventory.entity.GoodsReceiptDetailEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository

public interface IGoodsReceiptDetailRepository extends JpaRepository<GoodsReceiptDetailEntity, Integer> {
}
