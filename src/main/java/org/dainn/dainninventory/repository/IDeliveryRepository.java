package org.dainn.dainninventory.repository;

import org.dainn.dainninventory.entity.DeliveryEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository

public interface IDeliveryRepository extends JpaRepository<DeliveryEntity, Integer> {
}
