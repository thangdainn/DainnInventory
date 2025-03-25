package org.dainn.dainninventory.dto.image;

import lombok.*;
import org.dainn.dainninventory.dto.AbstractDTO;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ImageDTO extends AbstractDTO {
    private Integer productId;
    private String url;
}
