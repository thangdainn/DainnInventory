package org.dainn.dainninventory.dto;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class DeliveryDetailDTO extends AbstractDTO{
    private Integer deliveryId;
    private Integer orderId;
}
