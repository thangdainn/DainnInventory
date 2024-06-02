package org.dainn.dainninventory.dto;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class GoodsReceiptDetailsDTO extends AbstractDTO{
    private double price;
    private Integer quantity;
    private Integer goodReceiptId;
    private Integer productId;
}
