package com.franco.curso.springboot.app.springboot_crud.controllers;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RestController;

import com.franco.curso.springboot.app.springboot_crud.entities.Product;
import com.franco.curso.springboot.app.springboot_crud.services.ProductService;

import jakarta.validation.Valid;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@RestController
@RequestMapping("/api/products")
public class ProductController {
    
    @Autowired
    private ProductService service;

    @GetMapping
    public List<Product> list() {
        return service.findAll();
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<?> view(@PathVariable Long id) {
        Optional<Product> productOptional = service.findById(id);
        if (productOptional.isPresent()) {
            return ResponseEntity.ok(productOptional.orElseThrow());
        }
        return ResponseEntity.notFound().build();//404
    }
    
    @PostMapping
    public ResponseEntity<?> create(@Valid @RequestBody Product product, BindingResult result) {
        if (result.hasFieldErrors()) {
            return validation(result);
        }
        // Product productNew = service.save(product); 
        return ResponseEntity.status(HttpStatus.CREATED).body(service.save(product));
    }

    private ResponseEntity<?> validation(BindingResult result) {
        // llave y valor. llave: name, description y price. value: mensaje de error de la validacion
        Map<String, String> errors = new HashMap<>();

        result.getFieldErrors().forEach(err -> {
            errors.put(err.getField(), "El campo " + err.getField() + " "+ err.getDefaultMessage());
        });
        
        return ResponseEntity.badRequest().body(errors);
    }

    @PutMapping("/{id}") 
    public ResponseEntity<?> update( @Valid @RequestBody Product product, BindingResult result, @PathVariable Long id ) {
        
        if (result.hasFieldErrors()) {
            return validation(result);
        }

        Optional<Product> productOptional = service.update(id, product);
        if (productOptional.isPresent()) {
            return ResponseEntity.status(HttpStatus.CREATED).body(productOptional.orElseThrow());
        }
        return ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id) {
        Optional<Product> productOptional = service.delete(id);
        if (productOptional.isPresent()) {
            return ResponseEntity.ok(productOptional.orElseThrow());
        }
        return ResponseEntity.notFound().build();
    }
}
