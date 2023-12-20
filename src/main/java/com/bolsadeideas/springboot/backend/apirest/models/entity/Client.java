package com.bolsadeideas.springboot.backend.apirest.models.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.io.Serializable;
import java.util.Date;

@Entity
@Table(name="client")
public class Client implements Serializable {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) //IDENTITY: AUTOINCREMENTAL - SEQUENCE PARA ORACLE/POSTGRESS
    private Long id;

    @NotEmpty(message = "No puede estar vacio") //NO PUEDE SER VACIO , message; SIRVE PARA CUSTOMIZAR EL MENSAJE DE ERROR
    @Size(min = 4, max = 20)
    @Column(nullable = false) //NO PUEDE SER NULO
    private String firstname;
    @NotEmpty //NO PUEDE SER VACIO
    private String lastname;
    @NotEmpty //NO PUEDE SER VACIO
    @Email
    @Column(nullable = false, unique = true)
    private String email;

    @NotNull //NO PUEDE SER VACIO
    @Column(name="create_at") //SE PUEDE OMITIR SI SE LLAMA IGUAL AL CAMPO DE LA BD, SE AGREGA PARA RELACIONAR CON EL CAMPO DE LA BD
    @Temporal(TemporalType.DATE) //PARA INDICAR EQUIVALENCIA A LA BD
    private Date createAt;

    private String foto;

    @PrePersist
    public void prePersist(){ //VA A EJECUTAR ANTES DEL LLAMADO A LA PERSISTENCIA
        createAt = new Date();
    }
    private static final long serialVersionUIDLONG = 1;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Date getCreateAt() {
        return createAt;
    }

    public void setCreateAt(Date createAt) {
        this.createAt = createAt;
    }

    public String getFoto() {
        return foto;
    }

    public void setFoto(String foto) {
        this.foto = foto;
    }
}
