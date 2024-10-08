package org.dainn.dainninventory.repository;

import org.dainn.dainninventory.entity.UserEntity;
import org.dainn.dainninventory.utils.enums.Provider;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface IUserRepository extends JpaRepository<UserEntity, Integer>, JpaSpecificationExecutor<UserEntity> {
    boolean existsByEmailAndProvider(String email, Provider provider);
    Optional<UserEntity> findByEmailAndStatus(String email, Integer status);

    Optional<UserEntity> findByEmailAndProviderAndStatus(String email, Provider provider, Integer status);
    Optional<UserEntity> findByEmailAndPasswordAndProvider(String email, String password, Provider provider);

    @Modifying
    @Query("UPDATE UserEntity u SET u.status = 0 WHERE u.id IN :ids")
    void deleteAllByIdInBatchCustom(@Param("ids") List<Integer> ids);

    List<UserEntity> findAllByStatus(Integer status);
}
