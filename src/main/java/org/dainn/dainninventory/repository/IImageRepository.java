package org.dainn.dainninventory.repository;

import org.dainn.dainninventory.entity.BrandEntity;
import org.dainn.dainninventory.entity.ImageEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository

public interface IImageRepository extends JpaRepository<ImageEntity, Integer> {
    List<ImageEntity> findByProductId(Integer productId);
    void deleteByProductId(Integer productId);
}
