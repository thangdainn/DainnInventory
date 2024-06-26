package org.dainn.dainninventory.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "order_details")
public class OrderDetailEntity extends BaseEntity{
    @Column(name = "quantity", nullable = false)
    @Min(1)
    private Integer quantity;

    @Column(name = "price", nullable = false, precision = 10)
    private BigDecimal price;

    @Column(name = "total", nullable = false, precision = 10)
    private BigDecimal total;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id")
    private OrderEntity order;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id")
    private ProductEntity product;
}
