package com.leticiapillar.coffeestore.api.services.impl;

import com.leticiapillar.coffeestore.api.dtos.CoffeeCrudDTO;
import com.leticiapillar.coffeestore.api.dtos.CoffeeDTO;
import com.leticiapillar.coffeestore.api.enums.CoffeeSize;
import com.leticiapillar.coffeestore.api.mappers.CoffeeMapper;
import com.leticiapillar.coffeestore.api.models.Coffee;
import com.leticiapillar.coffeestore.api.repositories.CoffeeRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static com.leticiapillar.coffeestore.api.mocks.MocksCofee.mockCoffee;
import static com.leticiapillar.coffeestore.api.mocks.MocksCofee.mockCoffeeCrudDTO;
import static com.leticiapillar.coffeestore.api.mocks.MocksCofee.mockCoffeeDTO;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@DisplayName("CoffeeServiceImpl Tests")
class CoffeeServiceImplTest {

    @Mock
    private CoffeeRepository coffeeRepository;

    @Mock
    private CoffeeMapper coffeeMapper;

    @InjectMocks
    private CoffeeServiceImpl coffeeService;

    private UUID coffeeId;
    private LocalDateTime now;

    @BeforeEach
    void setUp() {
        coffeeId = UUID.randomUUID();
        now = LocalDateTime.now();
    }

    @Nested
    @DisplayName("findAll() Tests")
    class FindAllTests {

        @Test
        @DisplayName("should return all coffees from repository")
        void shouldReturnAllCoffeesFromRepository() {
            Coffee coffeeA = mockCoffee(UUID.randomUUID(), "Coffee A", CoffeeSize.SMALL, 34.99, true, null);
            Coffee coffeeB = mockCoffee(UUID.randomUUID(), "Coffee B", CoffeeSize.MEDIUM, 54.99, true, null);
            List<Coffee> coffeeList = List.of(coffeeA, coffeeB);

            CoffeeDTO coffeeDTOA = mockCoffeeDTO(UUID.randomUUID(), "Coffee A", CoffeeSize.SMALL, 34.99, true, null);
            CoffeeDTO coffeeDTOB = mockCoffeeDTO(UUID.randomUUID(), "Coffee B", CoffeeSize.MEDIUM, 54.99, true, null);

            when(coffeeRepository.findAll()).thenReturn(coffeeList);
            when(coffeeMapper.toDTOList(coffeeList)).thenReturn(List.of(coffeeDTOA, coffeeDTOB));

            List<CoffeeDTO> result = coffeeService.findAll();

            assertThat(result).hasSize(2);
            assertThat(result.get(0).name()).isEqualTo(coffeeDTOA.name());
            assertThat(result.get(1).name()).isEqualTo(coffeeDTOB.name());
            verify(coffeeRepository, times(1)).findAll();
            verify(coffeeMapper, times(1)).toDTOList(coffeeList);
        }

        @Test
        @DisplayName("should return empty list when no coffees exist")
        void shouldReturnEmptyListWhenNoCoffeesExist() {
            when(coffeeRepository.findAll()).thenReturn(List.of());
            when(coffeeMapper.toDTOList(List.of())).thenReturn(List.of());

            List<CoffeeDTO> result = coffeeService.findAll();

            assertThat(result).isEmpty();
            verify(coffeeRepository, times(1)).findAll();
            verify(coffeeMapper, times(1)).toDTOList(List.of());
        }

        @Test
        @DisplayName("should call repository findAll exactly once")
        void shouldCallRepositoryFindAllExactlyOnce() {
            Coffee coffee = mockCoffee(UUID.randomUUID(), "Coffee A", CoffeeSize.SMALL, 34.99, true, null);
            CoffeeDTO coffeeDTO = mockCoffeeDTO(UUID.randomUUID(), "Coffee A", CoffeeSize.SMALL, 34.99, true, null);
            when(coffeeRepository.findAll()).thenReturn(List.of(coffee));
            when(coffeeMapper.toDTOList(any())).thenReturn(List.of(coffeeDTO));

            coffeeService.findAll();

            verify(coffeeRepository, times(1)).findAll();
        }
    }

    @Nested
    @DisplayName("findById() Tests")
    class FindByIdTests {

        @Test
        @DisplayName("should return coffee when found")
        void shouldReturnCoffeeWhenFound() {
            Coffee coffee = mockCoffee(coffeeId, "Coffee A", CoffeeSize.SMALL, 34.99, true, null);
            CoffeeDTO coffeeDTO = mockCoffeeDTO(coffeeId, "Coffee A", CoffeeSize.SMALL, 34.99, true, null);
            when(coffeeRepository.findById(coffeeId)).thenReturn(Optional.of(coffee));
            when(coffeeMapper.toDTO(coffee)).thenReturn(coffeeDTO);

            Optional<CoffeeDTO> result = coffeeService.findById(coffeeId);

            assertThat(result).isPresent();
            assertThat(result.get().name()).isEqualTo(coffeeDTO.name());
            assertThat(result.get().id()).isEqualTo(coffeeId);
            verify(coffeeRepository, times(1)).findById(coffeeId);
            verify(coffeeMapper, times(1)).toDTO(coffee);
        }

        @Test
        @DisplayName("should return empty optional when coffee not found")
        void shouldReturnEmptyOptionalWhenCoffeeNotFound() {
            UUID nonExistentId = UUID.randomUUID();
            when(coffeeRepository.findById(nonExistentId)).thenReturn(Optional.empty());

            Optional<CoffeeDTO> result = coffeeService.findById(nonExistentId);

            assertThat(result).isEmpty();
            verify(coffeeRepository, times(1)).findById(nonExistentId);
            verify(coffeeMapper, never()).toDTO(any());
        }

        @Test
        @DisplayName("should call repository with correct id")
        void shouldCallRepositoryWithCorrectId() {
            Coffee coffee = mockCoffee(coffeeId, "Coffee A", CoffeeSize.SMALL, 34.99, true, null);
            CoffeeDTO coffeeDTO = mockCoffeeDTO(coffeeId, "Coffee A", CoffeeSize.SMALL, 34.99, true, null);
            when(coffeeRepository.findById(any())).thenReturn(Optional.of(coffee));
            when(coffeeMapper.toDTO(any())).thenReturn(coffeeDTO);

            coffeeService.findById(coffeeId);

            verify(coffeeRepository).findById(eq(coffeeId));
        }

        @Test
        @DisplayName("should not map coffee when repository returns empty")
        void shouldNotMapCoffeeWhenRepositoryReturnsEmpty() {
            when(coffeeRepository.findById(coffeeId)).thenReturn(Optional.empty());

            coffeeService.findById(coffeeId);

            verify(coffeeMapper, never()).toDTO(any());
        }
    }

    @Nested
    @DisplayName("create() Tests")
    class CreateTests {

        @Test
        @DisplayName("should create coffee and return DTO")
        void shouldCreateCoffeeAndReturnDTO() {
            Coffee newCoffee = mockCoffee(coffeeId, "Coffee A", CoffeeSize.SMALL, 34.99, true, null);
            CoffeeDTO coffeeDTO = mockCoffeeDTO(coffeeId, "Coffee A", CoffeeSize.SMALL, 34.99, true, null);
            CoffeeCrudDTO coffeeCrudDTO = mockCoffeeCrudDTO("Coffee A", CoffeeSize.SMALL, 34.99);

            when(coffeeMapper.toModel(coffeeCrudDTO)).thenReturn(newCoffee);
            when(coffeeRepository.save(newCoffee)).thenReturn(newCoffee);
            when(coffeeMapper.toDTO(newCoffee)).thenReturn(coffeeDTO);

            CoffeeDTO result = coffeeService.create(coffeeCrudDTO);

            assertThat(result).isNotNull();
            assertThat(result.createdAt()).isNotNull();
            assertThat(result.name()).isEqualTo(coffeeDTO.name());
            assertThat(result.price()).isEqualTo(new BigDecimal(coffeeDTO.price().toString()));
            assertThat(result.enabled()).isEqualTo(coffeeDTO.enabled());
            verify(coffeeMapper, times(1)).toModel(coffeeCrudDTO);
            verify(coffeeRepository, times(1)).save(newCoffee);
            verify(coffeeMapper, times(1)).toDTO(newCoffee);
        }

        @Test
        @DisplayName("should convert CoffeeCrudDTO to Coffee model")
        void shouldConvertCoffeeCrudDTOToCoffeeModel() {
            Coffee coffee = mockCoffee(coffeeId, "Coffee A", CoffeeSize.SMALL, 34.99, true, null);
            CoffeeDTO coffeeDTO = mockCoffeeDTO(coffeeId, "Coffee A", CoffeeSize.SMALL, 34.99, true, null);
            CoffeeCrudDTO coffeeCrudDTO = mockCoffeeCrudDTO("Coffee A", CoffeeSize.SMALL, 34.99);
            when(coffeeMapper.toModel(coffeeCrudDTO)).thenReturn(coffee);
            when(coffeeRepository.save(any())).thenReturn(coffee);
            when(coffeeMapper.toDTO(any())).thenReturn(coffeeDTO);

            coffeeService.create(coffeeCrudDTO);

            verify(coffeeMapper).toModel(eq(coffeeCrudDTO));
        }

        @Test
        @DisplayName("should save coffee to repository")
        void shouldSaveCoffeeToRepository() {
            Coffee coffee = mockCoffee(coffeeId, "Coffee A", CoffeeSize.SMALL, 34.99, true, null);
            CoffeeDTO coffeeDTO = mockCoffeeDTO(coffeeId, "Coffee A", CoffeeSize.SMALL, 34.99, true, null);
            CoffeeCrudDTO coffeeCrudDTO = mockCoffeeCrudDTO("Coffee A", CoffeeSize.SMALL, 34.99);
            ArgumentCaptor<Coffee> coffeeCaptor = ArgumentCaptor.forClass(Coffee.class);
            when(coffeeMapper.toModel(coffeeCrudDTO)).thenReturn(coffee);
            when(coffeeRepository.save(any())).thenReturn(coffee);
            when(coffeeMapper.toDTO(any())).thenReturn(coffeeDTO);

            coffeeService.create(coffeeCrudDTO);

            verify(coffeeRepository).save(coffeeCaptor.capture());
            assertThat(coffeeCaptor.getValue()).isEqualTo(coffee);
        }

        @Test
        @DisplayName("should convert saved coffee to DTO")
        void shouldConvertSavedCoffeeToDTO() {
            Coffee coffee = mockCoffee(coffeeId, "Coffee A", CoffeeSize.SMALL, 34.99, true, null);
            CoffeeDTO coffeeDTO = mockCoffeeDTO(coffeeId, "Coffee A", CoffeeSize.SMALL, 34.99, true, null);
            CoffeeCrudDTO coffeeCrudDTO = mockCoffeeCrudDTO("Coffee A", CoffeeSize.SMALL, 34.99);
            when(coffeeMapper.toModel(coffeeCrudDTO)).thenReturn(coffee);
            when(coffeeRepository.save(any())).thenReturn(coffee);
            when(coffeeMapper.toDTO(coffee)).thenReturn(coffeeDTO);

            coffeeService.create(coffeeCrudDTO);

            verify(coffeeMapper).toDTO(eq(coffee));
        }
    }

    @Nested
    @DisplayName("update() Tests")
    class UpdateTests {

        @Test
        @DisplayName("should update coffee and return updated DTO")
        void shouldUpdateCoffeeAndReturnUpdatedDTO() {
            Coffee coffee = mockCoffee(coffeeId, "Coffee A", CoffeeSize.MEDIUM, 54.99, true, now);
            Coffee updatedCoffee = mockCoffee(coffeeId, "Coffee A", CoffeeSize.MEDIUM, 54.99, true, now);
            CoffeeDTO updatedDTO = mockCoffeeDTO(coffeeId, "Coffee A", CoffeeSize.MEDIUM, 54.99, true, now);
            CoffeeCrudDTO updateRequest = mockCoffeeCrudDTO("Coffee A", CoffeeSize.MEDIUM, 54.99);

            when(coffeeRepository.findById(coffeeId)).thenReturn(Optional.of(coffee));
            when(coffeeRepository.save(any())).thenReturn(updatedCoffee);
            when(coffeeMapper.toDTO(updatedCoffee)).thenReturn(updatedDTO);

            Optional<CoffeeDTO> result = coffeeService.update(coffeeId, updateRequest);

            assertThat(result).isPresent();
            assertThat(result.get().updatedAt()).isNotNull();
            assertThat(result.get().name()).isEqualTo(updatedDTO.name());
            assertThat(result.get().price()).isEqualTo(new BigDecimal(updatedDTO.price().toString()));
            verify(coffeeRepository).findById(coffeeId);
            verify(coffeeMapper).updateEntityFromDto(eq(updateRequest), any());
            verify(coffeeRepository).save(any());
            verify(coffeeMapper).toDTO(updatedCoffee);
        }

        @Test
        @DisplayName("should return empty optional when coffee not found")
        void shouldReturnEmptyOptionalWhenCoffeeNotFoundForUpdate() {
            UUID nonExistentId = UUID.randomUUID();
            CoffeeCrudDTO coffeeCrudDTO = mockCoffeeCrudDTO("Coffee A", CoffeeSize.MEDIUM, 54.99);
            when(coffeeRepository.findById(nonExistentId)).thenReturn(Optional.empty());

            Optional<CoffeeDTO> result = coffeeService.update(nonExistentId, coffeeCrudDTO);

            assertThat(result).isEmpty();
            verify(coffeeRepository).findById(nonExistentId);
            verify(coffeeMapper, never()).updateEntityFromDto(any(), any());
            verify(coffeeRepository, never()).save(any());
            verify(coffeeMapper, never()).toDTO(any());
        }

        @Test
        @DisplayName("should update existing coffee entity from DTO")
        void shouldUpdateExistingCoffeeEntityFromDTO() {
            Coffee coffee = mockCoffee(coffeeId, "Coffee A", CoffeeSize.MEDIUM, 54.99, true, now);
            CoffeeDTO coffeeDTO = mockCoffeeDTO(coffeeId, "Coffee A", CoffeeSize.MEDIUM, 54.99, true, now);
            CoffeeCrudDTO coffeeCrudDTO = mockCoffeeCrudDTO("Coffee A", CoffeeSize.MEDIUM, 54.99);
            ArgumentCaptor<Coffee> coffeeCaptor = ArgumentCaptor.forClass(Coffee.class);
            when(coffeeRepository.findById(coffeeId)).thenReturn(Optional.of(coffee));
            when(coffeeRepository.save(any())).thenReturn(coffee);
            when(coffeeMapper.toDTO(any())).thenReturn(coffeeDTO);

            coffeeService.update(coffeeId, coffeeCrudDTO);

            verify(coffeeMapper).updateEntityFromDto(eq(coffeeCrudDTO), coffeeCaptor.capture());
            assertThat(coffeeCaptor.getValue()).isEqualTo(coffee);
        }

        @Test
        @DisplayName("should save updated coffee")
        void shouldSaveUpdatedCoffee() {
            Coffee coffee = mockCoffee(coffeeId, "Coffee A", CoffeeSize.MEDIUM, 54.99, true, now);
            CoffeeDTO coffeeDTO = mockCoffeeDTO(coffeeId, "Coffee A", CoffeeSize.MEDIUM, 54.99, true, now);
            CoffeeCrudDTO coffeeCrudDTO = mockCoffeeCrudDTO("Coffee A", CoffeeSize.MEDIUM, 54.99);
            when(coffeeRepository.findById(coffeeId)).thenReturn(Optional.of(coffee));
            when(coffeeRepository.save(any())).thenReturn(coffee);
            when(coffeeMapper.toDTO(any())).thenReturn(coffeeDTO);

            coffeeService.update(coffeeId, coffeeCrudDTO);

            verify(coffeeRepository, times(1)).findById(coffeeId);
            verify(coffeeRepository).save(any());
        }
    }

    @Nested
    @DisplayName("activate() Tests")
    class ActivateTests {

        @Test
        @DisplayName("should activate coffee when found")
        void shouldActivateCoffeeWhenFound() {
            Coffee inactiveCoffee = mockCoffee(coffeeId, "Coffee A", CoffeeSize.MEDIUM, 54.99, false, now);

            when(coffeeRepository.findById(coffeeId)).thenReturn(Optional.of(inactiveCoffee));
            when(coffeeRepository.save(any())).thenReturn(inactiveCoffee);

            coffeeService.activate(coffeeId);

            verify(coffeeRepository).findById(coffeeId);
            verify(coffeeRepository).save(any());
        }

        @Test
        @DisplayName("should set enabled to true")
        void shouldSetEnabledToTrue() {
            Coffee inactiveCoffee = mockCoffee(coffeeId, "Coffee A", CoffeeSize.MEDIUM, 54.99, false, now);

            ArgumentCaptor<Coffee> coffeeCaptor = ArgumentCaptor.forClass(Coffee.class);

            when(coffeeRepository.findById(coffeeId)).thenReturn(Optional.of(inactiveCoffee));
            when(coffeeRepository.save(any())).thenReturn(inactiveCoffee);

            coffeeService.activate(coffeeId);

            verify(coffeeRepository).save(coffeeCaptor.capture());
            assertThat(coffeeCaptor.getValue().isEnabled()).isTrue();
        }

        @Test
        @DisplayName("should not save when coffee not found")
        void shouldNotSaveWhenCoffeeNotFound() {
            UUID nonExistentId = UUID.randomUUID();
            when(coffeeRepository.findById(nonExistentId)).thenReturn(Optional.empty());

            coffeeService.activate(nonExistentId);

            verify(coffeeRepository).findById(nonExistentId);
            verify(coffeeRepository, never()).save(any());
        }

        @Test
        @DisplayName("should call repository findById with correct id")
        void shouldCallRepositoryFindByIdWithCorrectId() {
            Coffee coffee = mockCoffee(coffeeId, "Coffee A", CoffeeSize.MEDIUM, 54.99, false, now);
            when(coffeeRepository.findById(any())).thenReturn(Optional.of(coffee));
            when(coffeeRepository.save(any())).thenReturn(coffee);

            coffeeService.activate(coffeeId);

            verify(coffeeRepository).findById(eq(coffeeId));
        }
    }

    @Nested
    @DisplayName("inactivate() Tests")
    class InactivateTests {

        @Test
        @DisplayName("should inactivate coffee when found")
        void shouldInactivateCoffeeWhenFound() {
            Coffee activeCoffee = mockCoffee(coffeeId, "Coffee A", CoffeeSize.MEDIUM, 54.99, true, now);
            when(coffeeRepository.findById(coffeeId)).thenReturn(Optional.of(activeCoffee));
            when(coffeeRepository.save(any())).thenReturn(activeCoffee);

            coffeeService.inactivate(coffeeId);

            verify(coffeeRepository).findById(coffeeId);
            verify(coffeeRepository).save(any());
        }

        @Test
        @DisplayName("should set enabled to false")
        void shouldSetEnabledToFalse() {
            Coffee activeCoffee = mockCoffee(coffeeId, "Coffee A", CoffeeSize.MEDIUM, 54.99, true, now);
            ArgumentCaptor<Coffee> coffeeCaptor = ArgumentCaptor.forClass(Coffee.class);

            when(coffeeRepository.findById(coffeeId)).thenReturn(Optional.of(activeCoffee));
            when(coffeeRepository.save(any())).thenReturn(activeCoffee);

            coffeeService.inactivate(coffeeId);

            verify(coffeeRepository).save(coffeeCaptor.capture());
            assertThat(coffeeCaptor.getValue().isEnabled()).isFalse();
        }

        @Test
        @DisplayName("should not save when coffee not found")
        void shouldNotSaveWhenCoffeeNotFound() {
            UUID nonExistentId = UUID.randomUUID();
            when(coffeeRepository.findById(nonExistentId)).thenReturn(Optional.empty());

            coffeeService.inactivate(nonExistentId);

            verify(coffeeRepository).findById(nonExistentId);
            verify(coffeeRepository, never()).save(any());
        }

        @Test
        @DisplayName("should call repository findById with correct id")
        void shouldCallRepositoryFindByIdWithCorrectId() {
            Coffee coffee = mockCoffee(coffeeId, "Coffee A", CoffeeSize.MEDIUM, 54.99, true, now);
            when(coffeeRepository.findById(any())).thenReturn(Optional.of(coffee));
            when(coffeeRepository.save(any())).thenReturn(coffee);

            coffeeService.inactivate(coffeeId);

            verify(coffeeRepository).findById(eq(coffeeId));
        }
    }
}

