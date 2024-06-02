package org.dainn.dainninventory.dto;

import lombok.*;

import java.sql.Date;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class TokenDTO {
    private Integer id;
    private String deviceInfo;
    private String refreshToken;
    private Date refreshTokenExpirationDate;
    private Integer userId;
}
