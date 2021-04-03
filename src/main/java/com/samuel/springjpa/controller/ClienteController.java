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
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.samuel.springjpa.aop.ApiLog;
import com.samuel.springjpa.models.entity.Cliente;
import com.samuel.springjpa.models.entity.Region;
import com.samuel.springjpa.models.services.ClienteService;

//@ApiLog
@RestController
@RequestMapping("/clientes")
public class ClienteController {

    private static final String VALIDATION_ERROR = "Error en el campo '%s': %s";

    @Autowired
    private ClienteService clienteService;

    @GetMapping
    public List<Cliente> index() {
        return clienteService.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> find(@PathVariable Long id) {
        Map<String, Object> response = new HashMap<>();

        try {
            Cliente cliente = clienteService.findById(id);

            if (cliente == null) {
                response.put("error", String.format("No existe un cliente con id %s", id));
                return new ResponseEntity<Map<String, Object>>(response, HttpStatus.NOT_FOUND);
            }

            return new ResponseEntity<Cliente>(cliente, HttpStatus.OK);
        } catch (DataAccessException e) {
            response.put("message", "Error al consultar los datos.");
            response.put("error", e.getMostSpecificCause().getMessage());
            return new ResponseEntity<Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping
    public ResponseEntity<?> create(@Valid @RequestBody Cliente cliente, BindingResult bindingResult) {
        Map<String, Object> response = new HashMap<String, Object>();

        if (bindingResult.hasErrors()) {
            return replyValidationErrors(bindingResult);
        }

        Cliente nuevoCliente = null;
        try {
            nuevoCliente = clienteService.save(cliente);
        } catch (DataAccessException e) {
            response.put("message", "Error al guardar el nuevo registro.");
            response.put("error", e.getMostSpecificCause().getMessage());
            return new ResponseEntity<Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }

        return new ResponseEntity<Cliente>(nuevoCliente, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> update(@Valid @RequestBody Cliente nuevoCliente, BindingResult bindingResult,
            @PathVariable Long id) {
        Map<String, Object> response = new HashMap<>();

        if (bindingResult.hasErrors()) {
            return replyValidationErrors(bindingResult);
        }

        Cliente clienteActual = null;
        try {
            clienteActual = clienteService.findById(id);

            if (clienteActual == null) {
                response.put("error", String.format("No existe un cliente con id %s", id));
                return new ResponseEntity<Map<String, Object>>(response, HttpStatus.NOT_FOUND);
            }
        } catch (DataAccessException e) {
            response.put("message", "Error al acceder a la base de datos.");
            response.put("error", e.getMostSpecificCause().getMessage());
            return new ResponseEntity<Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        clienteActual.setNombre(nuevoCliente.getNombre());
        clienteActual.setApellido(nuevoCliente.getApellido());
        clienteActual.setEmail(nuevoCliente.getEmail());

        return create(clienteActual, bindingResult);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable("id") Long id) {
        Map<String, Object> response = new HashMap<String, Object>();

        try {
            clienteService.delete(id);
        } catch (DataAccessException e) {
            response.put("menssage", String.format("No se pudo borrar el registro con id %s", id));
            response.put("error", e.getMessage());
            return new ResponseEntity<Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }

        response.put("message", "Resgistro borrado");
        return new ResponseEntity<Map<String, Object>>(response, HttpStatus.OK);
    }
    
    @GetMapping("/regiones")
    public List<Region> getRegiones(){
        return clienteService.findAllRegiones();
    }
    
    private ResponseEntity<Map<String, Object>> replyValidationErrors(BindingResult bindingResult){
        Map<String, Object> response = new HashMap<String, Object>();
        List<String> errorList = new ArrayList<>();
        bindingResult.getFieldErrors().forEach(error -> errorList
                .add(String.format(VALIDATION_ERROR, error.getField(), error.getDefaultMessage())));

        response.put("errors", errorList);
        return new ResponseEntity<Map<String, Object>>(response, HttpStatus.BAD_REQUEST);
    }
}
