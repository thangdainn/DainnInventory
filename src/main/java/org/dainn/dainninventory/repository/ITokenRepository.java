package org.dainn.dainninventory.repository;

import org.dainn.dainninventory.entity.BrandEntity;
import org.dainn.dainninventory.entity.TokenEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ITokenRepository extends JpaRepository<TokenEntity, Integer> {
    @Modifying
    @Query("UPDATE TokenEntity t SET t.refreshToken = :refreshToken WHERE t.id = :id")
    void updateRefreshToken(@Param("refreshToken") String refreshToken, @Param("id") Integer id);
    void deleteByUser_IdAndDeviceInfo(Integer userId, String deviceInfo);
    Optional<TokenEntity> findByRefreshToken(String refreshToken);
    Optional<TokenEntity> findByUser_IdAndDeviceInfo(Integer userId, String deviceInfo);

    void deleteByRefreshToken(String refreshToken);
}
