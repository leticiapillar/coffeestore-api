package com.leticiapillar.coffeestore.api.mappers;

import com.leticiapillar.coffeestore.api.dtos.CoffeeCrudDTO;
import com.leticiapillar.coffeestore.api.dtos.CoffeeDTO;
import com.leticiapillar.coffeestore.api.models.Coffee;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

import java.util.List;

@Mapper(componentModel = "spring")
public interface CoffeeMapper {
    CoffeeDTO toDTO(Coffee coffee);
    Coffee toModel(CoffeeCrudDTO dto);
    List<CoffeeDTO> toDTOList(List<Coffee> coffees);
    void updateEntityFromDto(CoffeeCrudDTO dto, @MappingTarget Coffee coffee);
}
