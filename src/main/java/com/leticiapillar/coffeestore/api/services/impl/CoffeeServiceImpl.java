package com.leticiapillar.coffeestore.api.services.impl;

import com.leticiapillar.coffeestore.api.dtos.CoffeeCrudDTO;
import com.leticiapillar.coffeestore.api.dtos.CoffeeDTO;
import com.leticiapillar.coffeestore.api.mappers.CoffeeMapper;
import com.leticiapillar.coffeestore.api.models.Coffee;
import com.leticiapillar.coffeestore.api.repositories.CoffeeRepository;
import com.leticiapillar.coffeestore.api.services.CoffeeService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@AllArgsConstructor
public class CoffeeServiceImpl implements CoffeeService {

    private final CoffeeRepository coffeeRepository;
    private final CoffeeMapper coffeeMapper;

    @Override
    public List<CoffeeDTO> findAll() {
        return coffeeMapper.toDTOList(coffeeRepository.findAll());
    }

    @Override
    public Optional<CoffeeDTO> findById(UUID id) {
        return coffeeRepository.findById(id)
                .map(coffeeMapper::toDTO);
    }

    @Override
    public CoffeeDTO create(CoffeeCrudDTO dto) {
        Coffee coffee = coffeeMapper.toModel(dto);
        return coffeeMapper.toDTO(coffeeRepository.save(coffee));
    }

    @Override
    public Optional<CoffeeDTO> update(UUID id, CoffeeCrudDTO dto) {
        return coffeeRepository.findById(id)
                .map(existingCoffee -> {
                    coffeeMapper.updateEntityFromDto(dto, existingCoffee);
                    Coffee updatedCoffee = coffeeRepository.save(existingCoffee);
                    return coffeeMapper.toDTO(updatedCoffee);
                });
    }

    @Override
    public void activate(UUID id) {
        coffeeRepository.findById(id)
                .ifPresent(coffee -> {
                    coffee.setEnabled(true);
                    coffeeRepository.save(coffee);
                });
    }

    @Override
    public void inactivate(UUID id) {
        coffeeRepository.findById(id)
                .ifPresent(coffee -> {
                    coffee.setEnabled(false);
                    coffeeRepository.save(coffee);
                });
    }
}
