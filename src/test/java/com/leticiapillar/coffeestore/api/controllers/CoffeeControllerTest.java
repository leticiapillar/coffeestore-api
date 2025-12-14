package com.leticiapillar.coffeestore.api.controllers;

import com.leticiapillar.coffeestore.api.dtos.CoffeeCrudDTO;
import com.leticiapillar.coffeestore.api.dtos.CoffeeDTO;
import com.leticiapillar.coffeestore.api.enums.CoffeeSize;
import com.leticiapillar.coffeestore.api.services.CoffeeService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import tools.jackson.databind.ObjectMapper;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static com.leticiapillar.coffeestore.api.mocks.MocksCofee.mockCoffeeCrudDTO;
import static com.leticiapillar.coffeestore.api.mocks.MocksCofee.mockCoffeeDTO;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(CoffeeController.class)
@DisplayName("CoffeeController Tests")
class CoffeeControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private CoffeeService coffeeService;

    @Autowired
    private ObjectMapper objectMapper;

    private UUID coffeeId;

    @BeforeEach
    void setUp() {
        coffeeId = UUID.randomUUID();
    }

    @Nested
    @DisplayName("GET /api/coffees")
    class FindAllTests {

        @Test
        @DisplayName("should return all coffees with status 200")
        void shouldReturnAllCoffeesWithStatus200() throws Exception {
            CoffeeDTO coffeeDTOA = mockCoffeeDTO(UUID.randomUUID(), "Coffee A", CoffeeSize.SMALL, 34.99, true, null);
            CoffeeDTO coffeeDTOB = mockCoffeeDTO(UUID.randomUUID(), "Coffee B", CoffeeSize.MEDIUM, 54.99, true, null);
            CoffeeDTO coffeeDTOC = mockCoffeeDTO(UUID.randomUUID(), "Coffee C", CoffeeSize.LARGE, 74.99, true, null);
            List<CoffeeDTO> coffeeList = List.of(coffeeDTOA, coffeeDTOB, coffeeDTOC);

            when(coffeeService.findAll()).thenReturn(coffeeList);

            mockMvc.perform(get("/api/coffees")
                    .contentType(MediaType.APPLICATION_JSON))
                    .andDo(print())
                    .andExpect(status().isOk())
                    .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                    .andExpect(jsonPath("$", hasSize(3)))
                    .andExpect(jsonPath("$[0].id", is(coffeeDTOA.id().toString())))
                    .andExpect(jsonPath("$[0].name", is(coffeeDTOA.name())))
                    .andExpect(jsonPath("$[0].size", is(coffeeDTOA.size().toString())))
                    .andExpect(jsonPath("$[0].price", is(coffeeDTOA.price())))
                    .andExpect(jsonPath("$[0].enabled", is(coffeeDTOA.enabled())))
                    .andExpect(jsonPath("$[1].name", is(coffeeDTOB.name())))
                    .andExpect(jsonPath("$[2].name", is(coffeeDTOC.name())));

            verify(coffeeService, times(1)).findAll();
        }

        @Test
        @DisplayName("should return empty list when no coffees exist")
        void shouldReturnEmptyListWhenNoCoffeesExist() throws Exception {
            when(coffeeService.findAll()).thenReturn(List.of());

            mockMvc.perform(get("/api/coffees")
                    .contentType(MediaType.APPLICATION_JSON))
                    .andDo(print())
                    .andExpect(status().isOk())
                    .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                    .andExpect(jsonPath("$", hasSize(0)));

            verify(coffeeService, times(1)).findAll();
        }

        @Test
        @DisplayName("should call service findAll exactly once")
        void shouldCallServiceFindAllExactlyOnce() throws Exception {
            when(coffeeService.findAll()).thenReturn(List.of(
                    mockCoffeeDTO(UUID.randomUUID(), "Coffee A", CoffeeSize.SMALL, 34.99, true, null)
            ));

            mockMvc.perform(get("/api/coffees"));

            verify(coffeeService, times(1)).findAll();
        }
    }

    @Nested
    @DisplayName("GET /api/coffees/{id}")
    class FindByIdTests {

        @Test
        @DisplayName("should return coffee by id with status 200")
        void shouldReturnCoffeeByIdWithStatus200() throws Exception {
            CoffeeDTO coffeeDTO = mockCoffeeDTO(UUID.randomUUID(), "Coffee A", CoffeeSize.SMALL, 34.99, true, null);
            when(coffeeService.findById(coffeeId)).thenReturn(Optional.of(coffeeDTO));

            mockMvc.perform(get("/api/coffees/{id}", coffeeId)
                    .contentType(MediaType.APPLICATION_JSON))
                    .andDo(print())
                    .andExpect(status().isOk())
                    .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                    .andExpect(jsonPath("$.id", is(coffeeDTO.id().toString())))
                    .andExpect(jsonPath("$.name", is(coffeeDTO.name())))
                    .andExpect(jsonPath("$.size", is(coffeeDTO.size().toString())))
                    .andExpect(jsonPath("$.price", is(coffeeDTO.price())))
                    .andExpect(jsonPath("$.enabled", is(coffeeDTO.enabled())));

            verify(coffeeService, times(1)).findById(coffeeId);
        }

        @Test
        @DisplayName("should return 404 when coffee not found")
        void shouldReturn404WhenCoffeeNotFound() throws Exception {
            when(coffeeService.findById(coffeeId)).thenReturn(Optional.empty());

            mockMvc.perform(get("/api/coffees/{id}", coffeeId)
                    .contentType(MediaType.APPLICATION_JSON))
                    .andDo(print())
                    .andExpect(status().isNotFound());

            verify(coffeeService, times(1)).findById(coffeeId);
        }

        @Test
        @DisplayName("should call service findById with correct id")
        void shouldCallServiceFindByIdWithCorrectId() throws Exception {
            CoffeeDTO coffeeDTO = mockCoffeeDTO(coffeeId, "Coffee A", CoffeeSize.SMALL, 34.99, true, null);
            when(coffeeService.findById(any(UUID.class))).thenReturn(Optional.of(coffeeDTO));

            mockMvc.perform(get("/api/coffees/{id}", coffeeId));

            verify(coffeeService, times(1)).findById(eq(coffeeId));
        }
    }

    @Nested
    @DisplayName("POST /api/coffees")
    class CreateTests {

        @Test
        @DisplayName("should create coffee and return 201 with location header")
        void shouldCreateCoffeeAndReturn201WithLocationHeader() throws Exception {
            CoffeeDTO coffeeDTO = mockCoffeeDTO(UUID.randomUUID(), "Coffee A", CoffeeSize.SMALL, 34.99, true, null);
            CoffeeCrudDTO coffeeCrudDTO = mockCoffeeCrudDTO("Coffee A", CoffeeSize.SMALL, 34.99);
            when(coffeeService.create(any(CoffeeCrudDTO.class))).thenReturn(coffeeDTO);

            mockMvc.perform(post("/api/coffees")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(coffeeCrudDTO)))
                    .andDo(print())
                    .andExpect(status().isCreated())
                    .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                    .andExpect(header().exists("Location"))
                    .andExpect(header().string("Location", org.hamcrest.Matchers.containsString("/api/coffees/")))
                    .andExpect(jsonPath("$.id", is(coffeeDTO.id().toString())))
                    .andExpect(jsonPath("$.name", is(coffeeDTO.name())))
                    .andExpect(jsonPath("$.size", is(coffeeDTO.size().toString())))
                    .andExpect(jsonPath("$.price", is(coffeeDTO.price())));

            verify(coffeeService, times(1)).create(any(CoffeeCrudDTO.class));
        }

        @Test
        @DisplayName("should create coffee with correct data from request body")
        void shouldCreateCoffeeWithCorrectDataFromRequestBody() throws Exception {
            CoffeeDTO coffeeDTO = mockCoffeeDTO(UUID.randomUUID(), "Coffee A", CoffeeSize.SMALL, 34.99, true, null);
            CoffeeCrudDTO coffeeCrudDTO = mockCoffeeCrudDTO("Coffee A", CoffeeSize.SMALL, 34.99);
            when(coffeeService.create(any(CoffeeCrudDTO.class))).thenReturn(coffeeDTO);

            mockMvc.perform(post("/api/coffees")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(coffeeCrudDTO)))
                    .andExpect(status().isCreated());

            verify(coffeeService, times(1)).create(eq(coffeeCrudDTO));
        }

        @Test
        @DisplayName("should return location header with created coffee id")
        void shouldReturnLocationHeaderWithCreatedCoffeeId() throws Exception {
            CoffeeDTO coffeeDTO = mockCoffeeDTO(UUID.randomUUID(), "Coffee A", CoffeeSize.SMALL, 34.99, true, null);
            CoffeeCrudDTO coffeeCrudDTO = mockCoffeeCrudDTO("Coffee A", CoffeeSize.SMALL, 34.99);
            when(coffeeService.create(any(CoffeeCrudDTO.class))).thenReturn(coffeeDTO);

            mockMvc.perform(post("/api/coffees")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(coffeeCrudDTO)))
                    .andExpect(status().isCreated())
                    .andExpect(header().string("Location", org.hamcrest.Matchers.endsWith("/" + coffeeDTO.id())));

            verify(coffeeService, times(1)).create(any(CoffeeCrudDTO.class));
        }

        @Test
        @DisplayName("should return created coffee in response body")
        void shouldReturnCreatedCoffeeInResponseBody() throws Exception {
            CoffeeDTO coffeeDTO = mockCoffeeDTO(UUID.randomUUID(), "Coffee A", CoffeeSize.SMALL, 34.99, true, null);
            CoffeeCrudDTO coffeeCrudDTO = mockCoffeeCrudDTO("Coffee A", CoffeeSize.SMALL, 34.99);
            when(coffeeService.create(any(CoffeeCrudDTO.class))).thenReturn(coffeeDTO);

            // Act & Assert
            mockMvc.perform(post("/api/coffees")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(coffeeCrudDTO)))
                    .andExpect(status().isCreated())
                    .andExpect(jsonPath("$.id").exists())
                    .andExpect(jsonPath("$.name").exists())
                    .andExpect(jsonPath("$.size").exists())
                    .andExpect(jsonPath("$.price").exists());
        }
    }

    @Nested
    @DisplayName("PUT /api/coffees/{id}")
    class UpdateTests {

        @Test
        @DisplayName("should update coffee and return 200 with updated data")
        void shouldUpdateCoffeeAndReturn200WithUpdatedData() throws Exception {
            CoffeeDTO updatedCoffee = mockCoffeeDTO(coffeeId, "Coffee A", CoffeeSize.MEDIUM, 54.99, true, LocalDateTime.now());
            CoffeeCrudDTO updateDTO = mockCoffeeCrudDTO("Coffee A", CoffeeSize.MEDIUM, 54.99);
            when(coffeeService.update(eq(coffeeId), any(CoffeeCrudDTO.class))).thenReturn(Optional.of(updatedCoffee));

            mockMvc.perform(put("/api/coffees/{id}", coffeeId)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(updateDTO)))
                    .andDo(print())
                    .andExpect(status().isOk())
                    .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                    .andExpect(jsonPath("$.id", is(coffeeId.toString())))
                    .andExpect(jsonPath("$.name", is(updatedCoffee.name())))
                    .andExpect(jsonPath("$.size", is(updatedCoffee.size().toString())))
                    .andExpect(jsonPath("$.price", is(updatedCoffee.price())));

            verify(coffeeService, times(1)).update(eq(coffeeId), any(CoffeeCrudDTO.class));
        }

        @Test
        @DisplayName("should return 404 when coffee to update not found")
        void shouldReturn404WhenCoffeeToUpdateNotFound() throws Exception {
            CoffeeCrudDTO coffeeCrudDTO = mockCoffeeCrudDTO("Coffee A", CoffeeSize.MEDIUM, 54.99);
            when(coffeeService.update(eq(coffeeId), any(CoffeeCrudDTO.class)))
                    .thenReturn(Optional.empty());

            mockMvc.perform(put("/api/coffees/{id}", coffeeId)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(coffeeCrudDTO)))
                    .andDo(print())
                    .andExpect(status().isNotFound());

            verify(coffeeService, times(1)).update(eq(coffeeId), any(CoffeeCrudDTO.class));
        }

        @Test
        @DisplayName("should call service update with correct id and data")
        void shouldCallServiceUpdateWithCorrectIdAndData() throws Exception {
            CoffeeDTO coffeeDTO = mockCoffeeDTO(coffeeId, "Coffee A", CoffeeSize.MEDIUM, 54.99, true, LocalDateTime.now());
            CoffeeCrudDTO coffeeCrudDTO = mockCoffeeCrudDTO("Coffee A", CoffeeSize.MEDIUM, 54.99);
            when(coffeeService.update(eq(coffeeId), any(CoffeeCrudDTO.class)))
                    .thenReturn(Optional.of(coffeeDTO));

            // Act
            mockMvc.perform(put("/api/coffees/{id}", coffeeId)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(coffeeCrudDTO)));

            // Assert
            verify(coffeeService, times(1)).update(eq(coffeeId), eq(coffeeCrudDTO));
        }
    }

    @Nested
    @DisplayName("PUT /api/coffees/{id}/activate")
    class ActivateTests {

        @Test
        @DisplayName("should activate coffee and return 204 No Content")
        void shouldActivateCoffeeAndReturn204NoContent() throws Exception {
            // Arrange
            doNothing().when(coffeeService).activate(coffeeId);

            // Act & Assert
            mockMvc.perform(put("/api/coffees/{id}/activate", coffeeId))
                    .andDo(print())
                    .andExpect(status().isNoContent());

            verify(coffeeService, times(1)).activate(coffeeId);
        }

        @Test
        @DisplayName("should call service activate with correct id")
        void shouldCallServiceActivateWithCorrectId() throws Exception {
            // Arrange
            doNothing().when(coffeeService).activate(any(UUID.class));

            // Act
            mockMvc.perform(put("/api/coffees/{id}/activate", coffeeId));

            // Assert
            verify(coffeeService, times(1)).activate(eq(coffeeId));
        }

        @Test
        @DisplayName("should not have response body on activate")
        void shouldNotHaveResponseBodyOnActivate() throws Exception {
            // Arrange
            doNothing().when(coffeeService).activate(coffeeId);

            // Act & Assert
            mockMvc.perform(put("/api/coffees/{id}/activate", coffeeId))
                    .andExpect(status().isNoContent())
                    .andExpect(content().string(""));

            verify(coffeeService, times(1)).activate(coffeeId);
        }
    }

    @Nested
    @DisplayName("DELETE /api/coffees/{id}/inactivate")
    class InactivateTests {

        @Test
        @DisplayName("should inactivate coffee and return 204 No Content")
        void shouldInactivateCoffeeAndReturn204NoContent() throws Exception {
            // Arrange
            doNothing().when(coffeeService).inactivate(coffeeId);

            // Act & Assert
            mockMvc.perform(delete("/api/coffees/{id}/inactivate", coffeeId))
                    .andDo(print())
                    .andExpect(status().isNoContent());

            verify(coffeeService, times(1)).inactivate(coffeeId);
        }

        @Test
        @DisplayName("should call service inactivate with correct id")
        void shouldCallServiceInactivateWithCorrectId() throws Exception {
            // Arrange
            doNothing().when(coffeeService).inactivate(any(UUID.class));

            // Act
            mockMvc.perform(delete("/api/coffees/{id}/inactivate", coffeeId));

            // Assert
            verify(coffeeService, times(1)).inactivate(eq(coffeeId));
        }

        @Test
        @DisplayName("should not have response body on inactivate")
        void shouldNotHaveResponseBodyOnInactivate() throws Exception {
            // Arrange
            doNothing().when(coffeeService).inactivate(coffeeId);

            // Act & Assert
            mockMvc.perform(delete("/api/coffees/{id}/inactivate", coffeeId))
                    .andExpect(status().isNoContent())
                    .andExpect(content().string(""));

            verify(coffeeService, times(1)).inactivate(coffeeId);
        }
    }
}

