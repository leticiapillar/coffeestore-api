package com.leticiapillar.coffeestore.api.controllers;

import com.leticiapillar.coffeestore.api.dtos.CoffeeCrudDTO;
import com.leticiapillar.coffeestore.api.dtos.CoffeeDTO;
import com.leticiapillar.coffeestore.api.services.CoffeeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/coffees")
@RequiredArgsConstructor
public class CoffeeController {

    private final CoffeeService coffeeService;

    @GetMapping
    public ResponseEntity<List<CoffeeDTO>> findAll() {
        return ResponseEntity.ok(coffeeService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<CoffeeDTO> findById(@PathVariable UUID id) {
        return coffeeService.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<CoffeeDTO> create(@RequestBody CoffeeCrudDTO dto) {
        CoffeeDTO createdCoffee = coffeeService.create(dto);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(createdCoffee.id())
                .toUri();
        return ResponseEntity.created(location).build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<CoffeeDTO> update(@PathVariable UUID id, @RequestBody CoffeeCrudDTO dto) {
        return coffeeService.update(id, dto)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}/activate")
    public ResponseEntity<Void> activate(@PathVariable UUID id) {
        coffeeService.activate(id);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{id}/inactivate")
    public ResponseEntity<Void> inactivate(@PathVariable UUID id) {
        coffeeService.inactivate(id);
        return ResponseEntity.noContent().build();
    }

}
