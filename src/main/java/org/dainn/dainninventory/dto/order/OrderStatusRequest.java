package org.dainn.dainninventory.dto.order;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.dainn.dainninventory.utils.enums.OrderStatus;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class OrderStatusRequest {
    @NotNull(message = "Order Id is required")
    private List<Integer> ids;
    @NotNull(message = "Status is required")
    private OrderStatus status;
}
