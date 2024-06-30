package org.dainn.dainninventory.dto;

import lombok.*;

import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TokenDTO {
    private Integer id;
    private String deviceInfo;
    private String refreshToken;
//    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date refreshTokenExpirationDate;
    private Integer userId;
}
