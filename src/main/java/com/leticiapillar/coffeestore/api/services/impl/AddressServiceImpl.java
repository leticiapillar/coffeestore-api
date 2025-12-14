package com.leticiapillar.coffeestore.api.services.impl;

import com.leticiapillar.coffeestore.api.repositories.AddressRepository;
import com.leticiapillar.coffeestore.api.services.AddressService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@AllArgsConstructor
public class AddressServiceImpl implements AddressService {

    private final AddressRepository addressRepository;

    @Override
    public void deletebyId(UUID id) {
        addressRepository.deleteById(id);
    }
}
