package org.dainn.dainninventory.repository;

import org.dainn.dainninventory.entity.BrandEntity;
import org.dainn.dainninventory.entity.CategoryEntity;
import org.springframework.data.jpa.repository.JpaRepository;


public interface IBrandRepository extends JpaRepository<BrandEntity, Integer> {
}
