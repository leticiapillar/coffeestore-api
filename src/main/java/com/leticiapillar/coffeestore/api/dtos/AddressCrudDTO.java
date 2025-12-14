package com.leticiapillar.coffeestore.api.dtos;


public record AddressCrudDTO(
        String street,
        String number,
        String complement,
        String neighborhood,
        String city,
        String state,
        String zipCode
) {
}
