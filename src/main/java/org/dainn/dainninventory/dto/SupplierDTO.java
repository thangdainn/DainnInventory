package org.dainn.dainninventory.dto;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class SuppliersDTO extends AbstractDTO{
    private String address;
    private String name;
    private String phone;
}
