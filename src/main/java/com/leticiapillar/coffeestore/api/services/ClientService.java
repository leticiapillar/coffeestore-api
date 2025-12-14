package com.leticiapillar.coffeestore.api.services;

import com.leticiapillar.coffeestore.api.dtos.ClientCrudDTO;
import com.leticiapillar.coffeestore.api.dtos.ClientDTO;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ClientService {
    List<ClientDTO> findAll();
    Optional<ClientDTO> findById(UUID id);
    ClientDTO create(ClientCrudDTO dto);
    Optional<ClientDTO> update(UUID id, ClientCrudDTO dto);
    void activate(UUID id);
    void inactivate(UUID id);
}
