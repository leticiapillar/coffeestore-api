package com.leticiapillar.coffeestore.api.dtos;


import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public record ClientDTO(
        UUID id,
        String name,
        String email,
        List<AddressDTO> addresses,
        boolean enabled,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {
}
