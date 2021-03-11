package com.samuel.springjpa.models.dao;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.samuel.springjpa.models.entity.Cliente;
import com.samuel.springjpa.models.entity.Region;

public interface IClienteDao extends CrudRepository<Cliente, Long> {

    @Query("FROM Region")
    public List<Region> findAllRegiones();
}
