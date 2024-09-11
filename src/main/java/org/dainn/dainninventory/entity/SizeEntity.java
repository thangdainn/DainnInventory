package org.dainn.dainninventory.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "sizes")
public class SizeEntity extends BaseEntity{
    @Column(name = "name", nullable = false, unique = true)
    private String name;

    @Column(name = "description", nullable = false)
    private String description;

    @OneToMany(mappedBy = "size")
    private List<OrderDetailEntity> orderDetails = new ArrayList<>();

    @OneToMany(mappedBy = "size")
    private List<ProductSizeEntity> productSizes = new ArrayList<>();

    @OneToMany(mappedBy = "size")
    private List<CartEntity> carts = new ArrayList<>();

    @OneToMany(mappedBy = "size")
    private List<GoodsReceiptDetailEntity> goodsReceiptDetails = new ArrayList<>();
}
