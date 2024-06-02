package org.dainn.dainninventory.dto;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class GoodsReceiptsDTO extends AbstractDTO{
    private String address;
    private String phone;
    private double total;
    private Integer supplierId;
}
