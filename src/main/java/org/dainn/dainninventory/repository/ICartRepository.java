package org.dainn.dainninventory.repository;

import org.dainn.dainninventory.entity.CartEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ICartRepository extends JpaRepository<CartEntity, Integer> {
//    @Modifying
//    @Query("UPDATE CartEntity c SET c.quantity = :quantity WHERE c.id = :id")
//    int updateQuantity(@Param("quantity") Integer quantity, @Param("id") Integer id);
    List<CartEntity> findAllByUserId(Integer userId);
    Optional<CartEntity> findByUserIdAndProductId(Integer userId, Integer productId);

    void deleteAllByUserId(Integer userId);
}
