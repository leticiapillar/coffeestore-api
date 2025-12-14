package com.leticiapillar.coffeestore.api.repositories;

import com.leticiapillar.coffeestore.api.models.Address;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface AddressRepository extends JpaRepository<Address, UUID> {
    List<Address> findByClientId(UUID id);
}
