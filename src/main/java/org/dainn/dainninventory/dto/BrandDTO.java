package org.dainn.dainninventory.dto;

import lombok.*;

import java.sql.Date;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class BrandsDTO extends AbstractDTO{
    private String description;
    private String name;

}
