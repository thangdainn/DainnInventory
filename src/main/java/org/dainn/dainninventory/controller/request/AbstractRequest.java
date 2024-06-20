package org.dainn.dainninventory.controller.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public abstract class AbstractRequest {
    private Integer id;
    private Integer status = 1;
    private LocalDateTime createdDate;
    private LocalDateTime modifiedDate;
}


