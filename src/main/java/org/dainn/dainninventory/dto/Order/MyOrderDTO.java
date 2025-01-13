package org.dainn.dainninventory.dto.Order;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.dainn.dainninventory.dto.OrderDetailDTO;
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
public class MyOrderDTO {
    private Integer id;
    private String customerName;
    private String customerPhone;
    private String shippingAddress;
    private BigDecimal totalAmount;
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date orderDate;
    private PaymentMethod paymentMethod;
    private Boolean isPaid;
    private OrderStatus status;
    private List<OrderDetailDTO> details = new ArrayList<>();
}
