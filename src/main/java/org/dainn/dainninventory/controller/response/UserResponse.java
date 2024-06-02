package org.dainn.dainninventory.controller.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserResponse {
    private Integer id;
    private String email;
    private String name;
    private String providerId;
    private Integer status;
    private LocalDateTime createdDate;
    private LocalDateTime modifiedDate;
    private List<String> rolesName = new ArrayList<>();
}
