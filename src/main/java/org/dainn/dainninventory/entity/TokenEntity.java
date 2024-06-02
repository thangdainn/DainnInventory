package org.dainn.dainninventory.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.sql.Timestamp;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "tokens")
public class TokenEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @Column(name = "refresh_token", nullable = false)
    private String refreshToken;

    @Column(name = "refresh_token_expiration_date", nullable = false)
    private Timestamp refreshTokenExpirationDate;

    @Column(name = "device_info", nullable = false)
    private String deviceInfo;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private UserEntity user;
}
