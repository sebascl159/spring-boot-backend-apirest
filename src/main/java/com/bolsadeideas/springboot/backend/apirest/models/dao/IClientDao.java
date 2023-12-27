package com.bolsadeideas.springboot.backend.apirest.models.dao;

import com.bolsadeideas.springboot.backend.apirest.models.entity.Client;
import org.springframework.data.jpa.repository.JpaRepository;

/*
    Nombre Clase: ICliente -> I porque es una Interface
    Component: Spring Data JPA
        - Trae todos los metodos basicos para el CRUD, extendiendo de CrudRepository
        - Documentacion: https://docs.spring.io/spring-data/jpa/reference/repositories/core-concepts.html
                         https://docs.spring.io/spring-data/jpa/reference/jpa/query-methods.html
                         https://docs.spring.io/spring-data/jpa/reference/repositories/query-methods-details.html

 */

public interface IClientDao extends JpaRepository<Client, Long> {
    //Extender de CrudRepository<NombreDeLaTabla, TipoDatoDelIdPK>

}
