package com.leticiapillar.coffeestore.api.repositories;

import com.leticiapillar.coffeestore.api.models.Client;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface ClientRepository extends JpaRepository<Client, UUID> {
}
