package org.dainn.dainninventory.dto.order;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.dainn.dainninventory.dto.request.PageRequest;
import org.dainn.dainninventory.utils.enums.OrderStatus;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class MyOrderPageRequest extends PageRequest {
    private String keyword;
    private Integer userId;
    private OrderStatus status;
}
