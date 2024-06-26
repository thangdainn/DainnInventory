package org.dainn.dainninventory.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class GoodsReceiptDTO extends AbstractDTO{
    @NotNull(message = "Total amount is required")
    private BigDecimal totalAmount;

    @NotNull(message = "User id is required")
    private Integer userId;

    @NotNull(message = "Supplier id is required")
    private Integer supplierId;

    @Valid
    private List<GoodsReceiptDetailDTO> detailDTOS = new ArrayList<>();
}
