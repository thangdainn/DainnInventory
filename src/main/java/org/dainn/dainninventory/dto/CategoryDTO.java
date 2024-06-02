package org.dainn.dainninventory.dto;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CategoriesDTO extends AbstractDTO{
    private String description;
    private String name;
}
