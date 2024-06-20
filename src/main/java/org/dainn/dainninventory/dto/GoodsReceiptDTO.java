package org.dainn.dainninventory.dto;

import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class GoodsReceiptDTO extends AbstractDTO{
    @NotNull(message = "Total amount is required")
    private double totalAmount;

    @NotNull(message = "User id is required")
    private Integer userId;

    @NotNull(message = "Supplier id is required")
    private Integer supplierId;

    private List<GoodsReceiptDetailDTO> detailDTOS = new ArrayList<>();
}
