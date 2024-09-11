package org.dainn.dainninventory.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.dainn.dainninventory.utils.enums.OrderStatus;
import org.dainn.dainninventory.utils.enums.PaymentMethod;
import org.springframework.format.annotation.DateTimeFormat;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
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
    private BigDecimal totalAmount;

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date orderDate;

    @NotNull(message = "Payment method is required")
    private PaymentMethod paymentMethod;

    private Boolean isPaid = false;

    private OrderStatus status = OrderStatus.PROCESSING;

    @NotNull(message = "User is required")
    private Integer userId;

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date modifiedDate;

    @Valid
    private List<OrderDetailDTO> details = new ArrayList<>();
}
