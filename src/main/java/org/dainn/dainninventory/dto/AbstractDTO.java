package org.dainn.dainninventory.dto;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public abstract class AbstractDTO {
    private Integer id;
    private Integer status;
    private LocalDateTime createdDate;
    private LocalDateTime modifiedDate;
}


