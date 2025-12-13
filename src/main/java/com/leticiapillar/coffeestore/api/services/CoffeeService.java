package com.leticiapillar.coffeestore.api.services;

import com.leticiapillar.coffeestore.api.dtos.CoffeeCrudDTO;
import com.leticiapillar.coffeestore.api.dtos.CoffeeDTO;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface CoffeeService {
    List<CoffeeDTO> findAll();
    Optional<CoffeeDTO> findById(UUID id);
    CoffeeDTO create(CoffeeCrudDTO dto);
    Optional<CoffeeDTO> update(UUID id, CoffeeCrudDTO dto);
    void activate(UUID id);
    void inactivate(UUID id);
}
