package com.leticiapillar.coffeestore.api.mappers;

import com.leticiapillar.coffeestore.api.dtos.AddressCrudDTO;
import com.leticiapillar.coffeestore.api.dtos.AddressDTO;
import com.leticiapillar.coffeestore.api.models.Address;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface AddressMapper {
    AddressDTO toDTO(Address address);
    Address toModel(AddressCrudDTO dto);
    List<AddressDTO> toDTOList(List<Address> addresses);
}
