package com.leticiapillar.coffeestore.api.controllers;

import com.leticiapillar.coffeestore.api.services.AddressService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping("/api/adresses")
@RequiredArgsConstructor
public class AddressController {

    private final AddressService addressService;

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        addressService.deletebyId(id);
        return ResponseEntity.noContent().build();
    }

}
