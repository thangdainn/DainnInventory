package org.dainn.dainninventory.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CartDTO {
    private Integer id;
    private Integer quantity;
    private Integer productId;
    private Integer userId;
}
