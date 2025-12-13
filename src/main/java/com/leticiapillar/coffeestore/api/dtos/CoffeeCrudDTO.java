package com.leticiapillar.coffeestore.api.dtos;

import com.leticiapillar.coffeestore.api.enums.CoffeeSize;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

public record CoffeeCrudDTO(
        String name,
        CoffeeSize size,
        BigDecimal price
) {
}
