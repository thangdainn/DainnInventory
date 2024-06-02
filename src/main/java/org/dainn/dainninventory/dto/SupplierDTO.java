package org.dainn.dainninventory.dto;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class SupplierDTO extends AbstractDTO{
    private String address;
    private String name;
    private String phone;
}
