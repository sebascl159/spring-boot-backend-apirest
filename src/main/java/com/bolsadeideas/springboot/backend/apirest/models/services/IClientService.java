package com.bolsadeideas.springboot.backend.apirest.models.services;

import com.bolsadeideas.springboot.backend.apirest.models.entity.Client;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface IClientService {

    public List<Client> findAll();

    public Page<Client> findAll(Pageable pageable);

    public Optional<Client> findById(Long id);

    public Client save(Client client);

    //public Optional<Client> delete(Long id);
    public void delete(Long id);


}
