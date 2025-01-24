package org.dainn.dainninventory.repository;

import org.dainn.dainninventory.entity.CartEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ICartRepository extends JpaRepository<CartEntity, Integer> {
    Page<CartEntity> findAllByUserId(Integer userId, Pageable pageable);
    Optional<CartEntity> findByUserIdAndProductIdAndSizeId(Integer userId, Integer productId, Integer sizeId);
    void deleteAllByUserId(Integer userId);
    int countByUserId(Integer userId);
}
