package org.dainn.dainninventory.entity;

import jakarta.persistence.*;
import lombok.*;
import org.dainn.dainninventory.utils.ProviderId;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "users")
public class UserEntity extends BaseEntity{
    @Column(name = "email", nullable = false)
    private String email;

    @Column(name = "password")
    private String password;

    @Column(name = "name", nullable = false)
    private String name;

    @Enumerated(EnumType.STRING)
    @Column(name = "provider_id", nullable = false)
    private ProviderId providerId;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "user_role",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id"))
    private List<RoleEntity> roles = new ArrayList<>();

    @OneToMany(mappedBy = "user")
    private List<TokenEntity> tokens = new ArrayList<>();

    @OneToMany(mappedBy = "user")
    private List<GoodsReceiptEntity> goodsReceipts = new ArrayList<>();
}
