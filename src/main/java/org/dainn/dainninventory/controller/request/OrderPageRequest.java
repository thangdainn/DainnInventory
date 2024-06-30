package org.dainn.dainninventory.controller.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.dainn.dainninventory.utils.enums.OrderStatus;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class OrderPageRequest extends PageRequest{
    private String keyword;
    private Integer userId;
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date fromDate;
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date toDate;
    private OrderStatus status;
}
