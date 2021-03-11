package com.samuel.springjpa.models.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.samuel.springjpa.models.dao.IClienteDao;
import com.samuel.springjpa.models.entity.Cliente;
import com.samuel.springjpa.models.entity.Region;

@Service
public class ClienteService {
    private IClienteDao clienteDao;
    
    @Autowired
    public ClienteService(IClienteDao clienteDao) {
        this.clienteDao = clienteDao;
    }
    
    public List<Cliente> findAll(){
        return (List<Cliente>) clienteDao.findAll();
    }
    
    public Cliente save(Cliente cliente){
        return clienteDao.save(cliente);
    }
    
    public void delete(Long id) {
        clienteDao.deleteById(id);
    }
    
    public Cliente findById(Long id) {
        return clienteDao.findById(id).orElse(null);
    }

    public List<Region> findAllRegiones(){
        return clienteDao.findAllRegiones();
    }
}
