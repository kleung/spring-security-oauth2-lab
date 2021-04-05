package com.test.authorizationserver.model.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;


@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "AUTHORITIES", schema = "PUBLIC", catalog = "OAUTH2USER")
public class Authority {
    @Id
    @Column(name = "ID", nullable = false)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "authorityGenerator")
    @SequenceGenerator(
            name = "authorityGenerator",
            sequenceName = "AUTHORITY_SEQ",
            allocationSize = 1
    )
    private Long id;

    @Column(name = "AUTHORITY", nullable = false, length = 50)
    private String authority;

}
