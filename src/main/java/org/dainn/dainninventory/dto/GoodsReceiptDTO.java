package org.dainn.dainninventory.dto;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class GoodsReceiptDTO extends AbstractDTO{
    private String address;
    private String phone;
    private double total;
    private Integer supplierId;
}
