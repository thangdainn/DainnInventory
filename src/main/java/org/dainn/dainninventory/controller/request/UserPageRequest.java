package org.dainn.dainninventory.controller.request;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserPageRequest extends PageRequest{
    private String keyword;
}
