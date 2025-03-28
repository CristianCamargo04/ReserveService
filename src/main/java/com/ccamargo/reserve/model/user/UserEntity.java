package com.ccamargo.reserve.model.user;

import com.ccamargo.reserve.model.rol.RolEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity(name = "users")
@Table(indexes = {@Index(columnList = "email", name = "index_email", unique = true) })
public class UserEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false)
    @NotEmpty
    @Email
    private String email;

    @Column(name = "encrypted_password",nullable = false)
    @NotEmpty
    private String encryptedPassword;

    @ManyToOne(fetch=FetchType.EAGER)
    @JoinColumn(name = "authority_id", nullable = false)
    private RolEntity rol;

}
