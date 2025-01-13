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
public class MyOrderPageRequest extends PageRequest{
    private String keyword;
    private Integer userId;
    private OrderStatus status;
}
