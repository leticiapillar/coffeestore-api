package com.leticiapillar.coffeestore.api.services.impl;

import com.leticiapillar.coffeestore.api.dtos.ClientCrudDTO;
import com.leticiapillar.coffeestore.api.dtos.ClientDTO;
import com.leticiapillar.coffeestore.api.mappers.ClientMapper;
import com.leticiapillar.coffeestore.api.models.Client;
import com.leticiapillar.coffeestore.api.repositories.ClientRepository;
import com.leticiapillar.coffeestore.api.services.ClientService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@AllArgsConstructor
public class ClientServiceImpl implements ClientService {

    private final ClientRepository clientRepository;
    private final ClientMapper clientMapper;

    @Override
    public List<ClientDTO> findAll() {
        return clientMapper.toDTOList(clientRepository.findAll());
    }

    @Override
    public Optional<ClientDTO> findById(UUID id) {
        return clientRepository.findById(id)
                .map(clientMapper::toDTO);
    }

    @Override
    public ClientDTO create(ClientCrudDTO dto) {
        Client client = clientMapper.toModel(dto);
        return clientMapper.toDTO(clientRepository.save(client));
    }

    @Override
    public Optional<ClientDTO> update(UUID id, ClientCrudDTO dto) {
        return clientRepository.findById(id)
                .map(existingClient -> {
                    clientMapper.updateEntityFromDto(dto, existingClient);
                    Client updatedClient = clientRepository.save(existingClient);
                    return clientMapper.toDTO(updatedClient);
                });
    }

    @Override
    public void activate(UUID id) {
        clientRepository.findById(id)
                .ifPresent(client -> {
                    client.setEnabled(true);
                    clientRepository.save(client);
                });
    }

    @Override
    public void inactivate(UUID id) {
        clientRepository.findById(id)
                .ifPresent(client -> {
                    client.setEnabled(false);
                    clientRepository.save(client);
                });
    }

}
