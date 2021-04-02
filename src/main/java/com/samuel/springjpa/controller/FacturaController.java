package com.samuel.springjpa.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.samuel.springjpa.models.entity.Cliente;
import com.samuel.springjpa.models.entity.Factura;
import com.samuel.springjpa.models.services.ClienteService;
import com.samuel.springjpa.models.services.FacturaService;

@RestController
@RequestMapping("/facturas")
public class FacturaController {

    @Autowired
    private FacturaService facturaService;
    @Autowired
    private ClienteService clienteService;

    @GetMapping
    public List<Factura> getAll() {
        return facturaService.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getFactura(@PathVariable Long id) {
        Map<String, Object> response = new HashMap<>();
        ResponseEntity<?> responseEntity = null;
        Factura factura = null;

        try {
            factura = facturaService.findById(id);

            if (factura == null) {
                response.put("error", "No existe factura con id " + id);
                responseEntity = new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
            } else {
                responseEntity = new ResponseEntity<>(factura, HttpStatus.OK);
            }

        } catch (DataAccessException e) {
            response.put("error", "Error al buscar la factura " + id);
            response.put("message", e.getMessage());
            responseEntity = new ResponseEntity<>(response, HttpStatus.OK);
        }
        return responseEntity;
    }

    @GetMapping("/bycliente/{clienteid}")
    public ResponseEntity<?> getFacturasByCliente(@PathVariable Long clienteid) {
        Map<String, Object> response = new HashMap<>();
        ResponseEntity<?> responseEntity = null;
        List<Factura> facturaList = new ArrayList<>();

        try {
            facturaList.addAll(facturaService.findFacturasByCliente(clienteid));

            responseEntity = new ResponseEntity<>(facturaList, HttpStatus.OK);

        } catch (DataAccessException e) {
            response.put("error", "Error al buscar las facturas con cliente " + clienteid);
            response.put("message", e.getMessage());
            responseEntity = new ResponseEntity<>(response, HttpStatus.OK);
        }
        return responseEntity;
    }

    @PostMapping
    public ResponseEntity<?> create(@Valid @RequestBody Factura factura, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return replyValidationErrors(bindingResult);
        }
        Map<String, Object> response = new HashMap<>();
        ResponseEntity<?> responseEntity = null;

        try {
            Cliente cliente = clienteService.findById(factura.getCliente().getId());
            if (cliente == null) {
                response.put("error", "No existe cliente con id " + factura.getCliente().getId());
                responseEntity = new ResponseEntity<Map<String, Object>>(response, HttpStatus.BAD_REQUEST);
            } else {
                factura.setCliente(cliente);
                Factura nuevaFactura = facturaService.save(factura);
                responseEntity = new ResponseEntity<Factura>(nuevaFactura, HttpStatus.CREATED);
            }
        } catch (DataAccessException e) {
            response.put("error", "Error al guardar factura");
            response.put("message", e.getMessage());
            responseEntity = new ResponseEntity<Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }

        return responseEntity;
    }

    private ResponseEntity<?> replyValidationErrors(BindingResult bindingResult) {
        Map<String, List<String>> response = new HashMap<>();
        List<String> errorList = new ArrayList<>();
        bindingResult.getFieldErrors().forEach(
                error -> errorList.add("Error en el campo " + error.getField() + ": " + error.getDefaultMessage()));
        response.put("errors", errorList);
        return new ResponseEntity<>(response, HttpStatus.FORBIDDEN);
    }
}
