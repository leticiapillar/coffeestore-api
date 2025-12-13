package com.leticiapillar.coffeestore.api.repositories;

import com.leticiapillar.coffeestore.api.models.Coffee;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface CoffeeRepository extends JpaRepository<Coffee, UUID> {
}
