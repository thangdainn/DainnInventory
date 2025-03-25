package org.dainn.dainninventory.dto.cart;

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
public class CartPageRequest extends PageRequest {
    private Integer userId;
    private OrderStatus status;
}
