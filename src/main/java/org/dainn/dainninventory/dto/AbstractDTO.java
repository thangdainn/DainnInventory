package org.dainn.dainninventory.controller.response;

import lombok.*;

import java.sql.Date;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public abstract class AbstractResponse<T> {
    private Integer id;
    private Integer status;
    private Date createdDate;
    private Date modifiedDate;
}
