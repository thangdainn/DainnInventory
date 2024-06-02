package org.dainn.dainninventory.repository;

import org.dainn.dainninventory.entity.CategoryEntity;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;


public interface ICategoryRepository extends JpaRepository<CategoryEntity, Integer> {
}
