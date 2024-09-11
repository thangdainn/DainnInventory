package org.dainn.dainninventory.repository;

import org.dainn.dainninventory.entity.BrandEntity;
import org.dainn.dainninventory.entity.SizeEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ISizeRepository extends JpaRepository<SizeEntity, Integer> {
    Optional<SizeEntity> findByName(String name);
    @Modifying
    @Query("UPDATE SizeEntity r SET r.status = 0 WHERE r.id IN :ids")
    void deleteAllByIdInBatchCustom(@Param("ids") List<Integer> ids);

    List<SizeEntity> findAllByStatus(Integer status);
    Page<SizeEntity> findAllByStatus(Integer status, Pageable pageable);

    Page<SizeEntity> findAllByNameContainingIgnoreCaseAndStatus(String name, Integer status, Pageable pageable);
}
