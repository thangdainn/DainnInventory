package org.dainn.dainninventory.entity;

import jakarta.persistence.*;
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
@Table(name = "goods_receipt_details")
public class GoodsReceiptDetailEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @Column(name = "price", nullable = false, precision = 10)
    private BigDecimal price;

    @Column(name = "quantity", nullable = false)
    private Integer quantity;

    @Column(name = "total", nullable = false, precision = 10)
    private BigDecimal total;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "goods_receipt_id")
    private GoodsReceiptEntity goodsReceipt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id")
    private ProductEntity product;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "size_id")
    private SizeEntity size;
}
