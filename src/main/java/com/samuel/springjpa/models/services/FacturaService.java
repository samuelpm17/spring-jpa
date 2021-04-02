package com.samuel.springjpa.models.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.samuel.springjpa.models.dao.IFacturaDao;
import com.samuel.springjpa.models.entity.Factura;

@Service
public class FacturaService {

    @Autowired
    private IFacturaDao facturaDao;
    
    public List<Factura> findAll(){
        return (List<Factura>) facturaDao.findAll();
    }
    
    public Factura findById(Long id) {
        return facturaDao.findById(id).orElse(null);
    }
    
    public void delete(Factura factura) {
        facturaDao.delete(factura);
    }
    
    public Factura save(Factura factura) {
        return facturaDao.save(factura);
    }
    
    public List<Factura> findFacturasByCliente(Long id){
        return facturaDao.findAllByClienteId(id);
    }
}
