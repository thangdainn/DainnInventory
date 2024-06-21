package org.dainn.dainninventory.controller.request;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.dainn.dainninventory.utils.OrderStatus;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class OrderStatusRequest {
    @NotNull(message = "Status is required")
    private OrderStatus status;
}
