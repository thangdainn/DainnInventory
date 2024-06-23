package org.dainn.dainninventory.repository;

import org.dainn.dainninventory.entity.BrandEntity;
import org.dainn.dainninventory.entity.RoleEntity;
import org.dainn.dainninventory.entity.SupplierEntity;
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
public interface ISupplierRepository extends JpaRepository<SupplierEntity, Integer> {
    Optional<SupplierEntity> findByName(String name);
    @Modifying
    @Query("UPDATE SupplierEntity r SET r.status = 0 WHERE r.id IN :ids")
    void deleteAllByIdInBatchCustom(@Param("ids") List<Integer> ids);
    List<SupplierEntity> findAllByStatus(Integer status);

    Page<SupplierEntity> findAllByStatus(Integer status, Pageable pageable);
    Page<SupplierEntity> findAllByNameContainingIgnoreCaseAndStatus(String name, Integer status, Pageable pageable);
}
