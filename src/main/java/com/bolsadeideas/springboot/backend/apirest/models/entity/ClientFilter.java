package com.bolsadeideas.springboot.backend.apirest.models.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ClientFilter {

    @Id
    @Column
    private Long id;

    @NotEmpty(message = "No puede estar vacio")
    @Column
    private String firstname;

    @NotEmpty(message = "No puede estar vacio")
    @Column
    private String lastname;

    @NotEmpty(message = "No puede estar vacio")
    @Column(nullable = false, unique = true)
    @Email
    private String email;

    @Column(name="create_at")
    private Date createAt;

    @Column
    private String usertype;

    @Column
    private Long userstatus;






}
