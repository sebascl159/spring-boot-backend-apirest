package com.bolsadeideas.springboot.backend.apirest.models.services;

import com.bolsadeideas.springboot.backend.apirest.models.dao.IClientDao;
import com.bolsadeideas.springboot.backend.apirest.models.entity.Client;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service //Estereotipo de Component, para que lo reconozca el framework
public class ClientServiceImpl implements IClientService {

    @Autowired
    private IClientDao clientDao;

    @Override
    @Transactional(readOnly = true)
    public List<Client> findAll() {
        return (List<Client>) clientDao.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Client> findAll(Pageable pageable) {
        return clientDao.findAll(pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Client> findById(Long id) {
        return clientDao.findById(id);
    }

    @Override
    @Transactional()
    public Client save(Client client) {
        return clientDao.save(client);
    }

    @Override
    @Transactional()
    public void delete(Long id) {
        //Optional<Client> client = clientDao.findById(id);
        //clientDao.deleteById(id);
        clientDao.deleteById(id);
    }

}
