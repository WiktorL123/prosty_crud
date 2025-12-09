package com.example.prosty_crud;

import com.example.prosty_crud.adapters.postgres.repository.CarRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.HashMap;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
@Testcontainers
class CarControllerIntegrationTest {

    @Container
    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:16-alpine")
            .withDatabaseName("testdb")
            .withUsername("test")
            .withPassword("test");

    @DynamicPropertySource
    static void configureDataSource(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgres::getJdbcUrl);
        registry.add("spring.datasource.username", postgres::getUsername);
        registry.add("spring.datasource.password", postgres::getPassword);

        // jeśli używasz JPA/Hibernate i nie masz Flyway/Liquibase:
        registry.add("spring.jpa.hibernate.ddl-auto", () -> "update");
    }

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    // repo tylko do czyszczenia i ew. dodatkowych asercji
    @Autowired
    private CarRepository carRepository;

    @BeforeEach
    void cleanDatabase() {
        carRepository.deleteAll();
    }

    private String createCarJson(String model, String color, String yearOfProduction) throws Exception {
        Map<String, Object> payload = new HashMap<>();
        // dopasuj klucze do pól w CreateCarDto
        payload.put("model", model);
        payload.put("color", color);
        payload.put("yearOfProduction", yearOfProduction);
        return objectMapper.writeValueAsString(payload);
    }

    private String updateCarJson(String model, String color, String yearOfProduction) throws Exception {
        Map<String, Object> payload = new HashMap<>();
        // dopasuj klucze do pól w UpdateCarDto
        payload.put("model", model);
        payload.put("color", color);
        payload.put("yearOfProduction", yearOfProduction);
        return objectMapper.writeValueAsString(payload);
    }

    @Test
    void createCar_shouldPersistAndReturnCreated() throws Exception {
        // given
        String body = createCarJson("Audi A4", "black", "2020");

        // when
        var result = mockMvc.perform(post("/cars")
                        .contentType(APPLICATION_JSON)
                        .content(body))
                // then
                .andExpect(status().isCreated())
                .andExpect(header().exists("Location"))
                .andExpect(jsonPath("$.id").isNotEmpty())
                .andExpect(jsonPath("$.model").value("Audi A4"))
                .andExpect(jsonPath("$.color").value("black"))
                .andExpect(jsonPath("$.yearOfProduction").value("2020"))
                .andReturn();

        // dodatkowa asercja na bazę
        assertThat(carRepository.count()).isEqualTo(1L);
    }

    @Test
    void getAll_shouldReturnCreatedCars() throws Exception {
        // najpierw wstrzelamy jeden rekord przez API
        String body = createCarJson("BMW M3", "blue", "2022");

        var createResult = mockMvc.perform(post("/cars")
                        .contentType(APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isCreated())
                .andReturn();

        // when + then
        mockMvc.perform(get("/cars")
                        .accept(APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].model").value("BMW M3"))
                .andExpect(jsonPath("$[0].color").value("blue"))
                .andExpect(jsonPath("$[0].yearOfProduction").value("2022"));
    }

    @Test
    void getCarById_shouldReturnCar() throws Exception {
        // najpierw create, żeby mieć ID
        String body = createCarJson("Skoda Octavia", "white", "2019");

        var createResult = mockMvc.perform(post("/cars")
                        .contentType(APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isCreated())
                .andReturn();

        String responseBody = createResult.getResponse().getContentAsString();
        // zakładamy, że CarResponseDto ma pole "id"
        String id = objectMapper.readTree(responseBody).get("id").asText();

        // when + then
        mockMvc.perform(get("/cars/{id}", id)
                        .accept(APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(id))
                .andExpect(jsonPath("$.model").value("Skoda Octavia"))
                .andExpect(jsonPath("$.color").value("white"))
                .andExpect(jsonPath("$.yearOfProduction").value("2019"));
    }

    @Test
    void updateCar_shouldModifyExistingCar() throws Exception {
        // create
        String createBody = createCarJson("Fiat Panda", "yellow", "2015");

        var createResult = mockMvc.perform(post("/cars")
                        .contentType(APPLICATION_JSON)
                        .content(createBody))
                .andExpect(status().isCreated())
                .andReturn();

        String responseBody = createResult.getResponse().getContentAsString();
        String id = objectMapper.readTree(responseBody).get("id").asText();

        // update
        String updateBody = updateCarJson("Fiat Panda", "red", "2016");

        mockMvc.perform(put("/cars/{id}", id)
                        .contentType(APPLICATION_JSON)
                        .content(updateBody))
                .andExpect(status().isCreated())
                .andExpect(header().string("Location", "/cars/" + id))
                .andExpect(jsonPath("$.id").value(id))
                .andExpect(jsonPath("$.color").value("red"))
                .andExpect(jsonPath("$.yearOfProduction").value("2016"));
    }

    @Test
    void deleteCar_shouldRemoveFromDatabase() throws Exception {
        // create
        String body = createCarJson("Toyota Corolla", "silver", "2018");

        var createResult = mockMvc.perform(post("/cars")
                        .contentType(APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isCreated())
                .andReturn();

        String responseBody = createResult.getResponse().getContentAsString();
        String id = objectMapper.readTree(responseBody).get("id").asText();

        // delete
        mockMvc.perform(delete("/cars/{id}", id))
                .andExpect(status().isNoContent());

        // baza pusta
        assertThat(carRepository.existsById(id)).isFalse();

        // GET po skasowanym może rzucać 404 – zależy jak masz obsłużone wyjątki
        // poniższe zostaw jako komentarz jeśli nie masz global exception handlera:
        // mockMvc.perform(get("/cars/{id}", id))
        //        .andExpect(status().isNotFound());
    }
}
