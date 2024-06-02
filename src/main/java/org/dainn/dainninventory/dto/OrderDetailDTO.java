package org.dainn.dainninventory.dto;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class OrderDetailsDTO extends AbstractDTO{
    private double price;
    private Integer quantity;
    private double total;
    private Integer orderId;
    private Integer productId;
}
