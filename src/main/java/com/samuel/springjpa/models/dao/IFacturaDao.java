package com.samuel.springjpa.models.dao;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.samuel.springjpa.models.entity.Factura;

public interface IFacturaDao extends CrudRepository<Factura, Long> {
    @Query(value = "SELECT f.* FROM facturas f WHERE f.cliente_id = ?1",nativeQuery = true)
    public List<Factura> findAllByClienteId(Long id);
}
