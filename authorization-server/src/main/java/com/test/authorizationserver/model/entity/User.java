package com.test.authorizationserver.model.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "USERS", schema = "PUBLIC", catalog = "OAUTH2USER")
public class User {

    @Id
    @Column(name = "ID", nullable = false)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "userGenerator")
    @SequenceGenerator(
            name = "userGenerator",
            sequenceName = "USER_SEQ",
            allocationSize = 1
    )
    private Long id;

    @Column(name = "USERNAME", nullable = false, length = 50)
    private String username;

    @Column(name = "PASSWORD", nullable = false, length = 200)
    private String password;

    @Column(name = "ENABLED", nullable = false)
    private Boolean enabled;

    @OneToMany(
            fetch = FetchType.EAGER,
            orphanRemoval = true,
            cascade = {CascadeType.ALL}
    )
    @JoinColumn(
            name = "USER_ID",
            referencedColumnName = "ID",
            updatable = false,
            nullable = false
    )
    private List<Authority> authorities;

}
