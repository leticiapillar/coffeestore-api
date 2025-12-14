package com.leticiapillar.coffeestore.api.mappers;

import com.leticiapillar.coffeestore.api.dtos.ClientCrudDTO;
import com.leticiapillar.coffeestore.api.dtos.ClientDTO;
import com.leticiapillar.coffeestore.api.models.Client;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ClientMapper {
    ClientDTO toDTO(Client client);
    Client toModel(ClientCrudDTO dto);
    List< ClientDTO> toDTOList(List<Client> clients);
    void updateEntityFromDto(ClientCrudDTO dto, @MappingTarget Client client);
}
