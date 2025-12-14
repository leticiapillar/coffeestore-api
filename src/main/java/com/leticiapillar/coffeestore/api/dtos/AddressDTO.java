package com.leticiapillar.coffeestore.api.dtos;


import java.util.UUID;

public record AddressDTO(
        UUID id,
        String street,
        String number,
        String complement,
        String neighborhood,
        String city,
        String state,
        String zipCode
) {
}
