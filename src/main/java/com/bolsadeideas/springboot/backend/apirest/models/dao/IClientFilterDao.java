package com.bolsadeideas.springboot.backend.apirest.models.dao;

import com.bolsadeideas.springboot.backend.apirest.models.entity.Client;
import com.bolsadeideas.springboot.backend.apirest.models.entity.ClientFilter;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.query.Procedure;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface IClientFilterDao extends JpaRepository<ClientFilter, Long> {

    @Procedure(name = "filtrarclientes")
    List<ClientFilter> filtrarclientes(@Param("tipousuario") String tipoUsuario,
                                       @Param("estado") Long estado);

}
