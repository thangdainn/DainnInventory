package org.dainn.dainninventory.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "order_detail")
public class OrderDetail extends BaseEntity{
    private int quantity;
    private double price;
    private double discount;
    private double total;
    private String note;
    private String status;
    private String orderCode;
    private String productCode;
}
