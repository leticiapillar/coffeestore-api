package com.leticiapillar.coffeestore.api.mocks;

import com.leticiapillar.coffeestore.api.dtos.CoffeeCrudDTO;
import com.leticiapillar.coffeestore.api.dtos.CoffeeDTO;
import com.leticiapillar.coffeestore.api.enums.CoffeeSize;
import com.leticiapillar.coffeestore.api.models.Coffee;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

public class MocksCofee {

    public static Coffee mockCoffee(UUID id, String name, CoffeeSize size, Double price, boolean enabled, LocalDateTime updatedAt) {
        return Coffee.builder()
                .id(id)
                .name(name)
                .size(size)
                .price(new BigDecimal(price))
                .enabled(enabled)
                .createdAt(LocalDateTime.now())
                .updatedAt(updatedAt)
                .build();
    }

    public static CoffeeDTO mockCoffeeDTO(UUID id, String name, CoffeeSize size, Double price, boolean enabled, LocalDateTime updatedAt) {
        return new CoffeeDTO(id, name, size, new BigDecimal(price), enabled, LocalDateTime.now(), updatedAt);
    }

    public static CoffeeCrudDTO mockCoffeeCrudDTO(String name, CoffeeSize size, Double price) {
        return new CoffeeCrudDTO(name, size, new BigDecimal(price));
    }

}
