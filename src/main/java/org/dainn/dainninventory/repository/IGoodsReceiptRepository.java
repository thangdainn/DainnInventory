package org.dainn.dainninventory.repository;

import org.dainn.dainninventory.entity.GoodsReceiptEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository

public interface IGoodsReceiptRepository extends JpaRepository<GoodsReceiptEntity, Integer> {
}
