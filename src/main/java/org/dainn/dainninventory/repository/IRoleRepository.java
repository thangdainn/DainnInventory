package org.dainn.dainninventory.repository;

import org.dainn.dainninventory.entity.CategoryEntity;
import org.dainn.dainninventory.entity.RoleEntity;
import org.dainn.dainninventory.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface IRoleRepository extends JpaRepository<RoleEntity, Integer> {
//    Optional<RoleEntity> findById(Integer id);
    Optional<RoleEntity> findByName(String name);
    List<RoleEntity> findByUsers(List<UserEntity> users);

    @Modifying
    @Query("UPDATE RoleEntity r SET r.status = 0 WHERE r.id IN :ids")
    void deleteAllByIdInBatchCustom(@Param("ids") List<Integer> ids);
}
