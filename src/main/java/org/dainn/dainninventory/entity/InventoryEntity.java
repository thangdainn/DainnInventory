package org.dainn.dainninventory.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "inventories")
public class InventoryEntity extends BaseEntity{
    @Column(name = "quantity", nullable = false)
    @Min(0)
    private Integer quantity;

    @Column(name = "reorder_point", nullable = false)
    @Min(0)
    private Integer reorderPoint;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id")
    private ProductEntity product;
}
