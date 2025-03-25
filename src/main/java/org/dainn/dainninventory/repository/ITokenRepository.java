package org.dainn.dainninventory.repository;

import org.dainn.dainninventory.entity.TokenEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ITokenRepository extends JpaRepository<TokenEntity, Integer> {
    Optional<TokenEntity> findByUserIdAndIpAddress(Integer userId, String ipAddress);

    @Modifying
    @Query("UPDATE TokenEntity t SET t.refreshToken = :refreshToken WHERE t.id = :id")
    void updateRefreshToken(@Param("refreshToken") String refreshToken, @Param("id") Integer id);

    Optional<TokenEntity> findByRefreshToken(String refreshToken);
    void deleteByRefreshToken(String refreshToken);
    void deleteByUser_Id(Integer userId);

    @Modifying
    @Query("DELETE FROM TokenEntity t WHERE t.user.id = :userId AND t.ipAddress != :ipAddress")
    void deleteByUserIdAndNotIpAddress(@Param("userId") Integer userId, @Param("ipAddress") String ipAddress);
}
