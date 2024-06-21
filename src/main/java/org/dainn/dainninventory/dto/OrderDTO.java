package org.dainn.dainninventory.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.dainn.dainninventory.utils.OrderStatus;
import org.dainn.dainninventory.utils.PaymentMethod;

import java.sql.Date;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class OrderDTO {
    private Integer id;
    @NotBlank(message = "Name is required")
    @NotNull(message = "Name is required")
    private String customerName;

    @NotBlank(message = "Phone is required")
    @NotNull(message = "Phone is required")
    private String customerPhone;

    @NotBlank(message = "Address is required")
    @NotNull(message = "Address is required")
    private String shippingAddress;

    @NotNull(message = "Total amount is required")
    private double totalAmount;

    private LocalDateTime orderDate;

    @NotNull(message = "Payment method is required")
    private PaymentMethod paymentMethod;

    private OrderStatus status = OrderStatus.PROCESSING;

    @NotNull(message = "User is required")
    private Integer userId;

    private LocalDateTime modifiedDate;

    @Valid
    private List<OrderDetailDTO> detailDTOS = new ArrayList<>();
}
