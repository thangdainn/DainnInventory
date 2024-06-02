package org.dainn.dainninventory.dto;

import lombok.*;
import org.dainn.dainninventory.utils.OrderStatus;

import java.sql.Date;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class OrdersDTO {
    private Integer id;
    private String customerName;
    private String customerPhone;
    private String note;
    private double totalAmount;
    private Date orderDate;
    private String paymentMethod;
    private String customerAddress;
    private OrderStatus status;
    private Integer deliveryId;
    private Integer userId;
    private Date modifiedDate;
}
