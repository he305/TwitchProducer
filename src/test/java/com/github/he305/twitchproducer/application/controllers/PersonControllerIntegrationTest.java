package com.github.he305.twitchproducer.application.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.he305.twitchproducer.application.constants.ApiVersionPathConstants;
import com.github.he305.twitchproducer.application.dto.PersonDtoListDto;
import com.github.he305.twitchproducer.application.dto.PersonResponseDto;
import com.github.he305.twitchproducer.common.dto.PersonAddDto;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.util.TestPropertyValues;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.transaction.annotation.Transactional;
import org.testcontainers.containers.PostgreSQLContainer;

import java.util.List;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ContextConfiguration(initializers = {PersonControllerIntegrationTest.Initializer.class})
class PersonControllerIntegrationTest {
    private static final PostgreSQLContainer sqlContainer;

    static {
        sqlContainer = new PostgreSQLContainer("postgres")
                .withDatabaseName("integration-tests-db")
                .withUsername("sa")
                .withPassword("sa");
        sqlContainer.start();
    }

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private PersonController underTest;

    private List<PersonResponseDto> injectPerson() {
        List<PersonAddDto> injected = List.of(
                new PersonAddDto("test", "test1"),
                new PersonAddDto("test2", "test3"),
                new PersonAddDto("test4", "test5")
        );

        return injected
                .stream()
                .map(p -> underTest.addPerson(p))
                .map(HttpEntity::getBody)
                .collect(Collectors.toList());
    }

    @Test
    @Transactional
    void getAll_existingData() throws Exception {
        List<PersonResponseDto> injectedData = injectPerson();

        ObjectMapper mapper = new ObjectMapper();
        MvcResult result = mockMvc
                .perform(get(ApiVersionPathConstants.V1 + "/person"))
                .andDo(print())
                .andReturn();

        PersonDtoListDto actual = mapper.readValue(result.getResponse().getContentAsString(), PersonDtoListDto.class);
        assertEquals(injectedData, actual.getPersons());
    }

    @Test
    void getAll_noData() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        MvcResult result = mockMvc
                .perform(get(ApiVersionPathConstants.V1 + "/person"))
                .andDo(print())
                .andReturn();

        PersonDtoListDto personDtoListDto = mapper.readValue(result.getResponse().getContentAsString(), PersonDtoListDto.class);
        assertTrue(personDtoListDto.getPersons().isEmpty());
    }

    @Test
    @Transactional
    void getById_success() throws Exception {
        List<PersonResponseDto> injectedData = injectPerson();
        ObjectMapper mapper = new ObjectMapper();

        PersonResponseDto expected = injectedData.get(0);
        MvcResult result = mockMvc
                .perform(get(String.format(ApiVersionPathConstants.V1 + "/person/%d", expected.getId())))
                .andDo(print())
                .andReturn();

        PersonResponseDto actual = mapper.readValue(result.getResponse().getContentAsString(), PersonResponseDto.class);
        assertEquals(expected, actual);
    }

    @Test
    @Transactional
    void getById_noResult() throws Exception {
        List<PersonResponseDto> injectedData = injectPerson();
        Long idToFind = 9999L;
        injectedData.forEach(s -> assertNotEquals(s.getId(), idToFind));

        MvcResult result = mockMvc
                .perform(get(String.format(ApiVersionPathConstants.V1 + "/person/%d", idToFind)))
                .andDo(print())
                .andReturn();

        assertEquals(HttpStatus.NOT_FOUND.value(), result.getResponse().getStatus());
        assertEquals("", result.getResponse().getContentAsString());
    }

    @Test
    @Transactional
    void addPerson_success() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        PersonAddDto data = new PersonAddDto("test", "test1");
        String jsonObject = mapper.writeValueAsString(data);

        MvcResult result = mockMvc
                .perform(post(ApiVersionPathConstants.V1 + "/person")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonObject))
                .andDo(print())
                .andReturn();

        assertEquals(HttpStatus.CREATED.value(), result.getResponse().getStatus());

        PersonResponseDto actual = mapper.readValue(result.getResponse().getContentAsString(), PersonResponseDto.class);
        PersonAddDto convertedActual = new PersonAddDto(actual.getFirstName(), actual.getLastName());
        assertEquals(data, convertedActual);
    }

    @Test
    @Transactional
    void addPerson_alreadyExists() throws Exception {
        List<PersonResponseDto> injectedData = injectPerson();

        ObjectMapper mapper = new ObjectMapper();
        PersonResponseDto data = injectedData.get(0);
        String jsonObject = mapper.writeValueAsString(data);

        MvcResult result = mockMvc
                .perform(post(ApiVersionPathConstants.V1 + "/person")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonObject))
                .andDo(print())
                .andReturn();

        assertEquals(HttpStatus.BAD_REQUEST.value(), result.getResponse().getStatus());
    }

    @Test
    @Transactional
    @SneakyThrows
    void deletePerson_notFound() {
        MvcResult result = mockMvc
                .perform(delete(ApiVersionPathConstants.V1 + String.format("/person/%d", 99999L)))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andReturn();
    }

    @Test
    @Transactional
    @SneakyThrows
    void deletePerson_success() {
        List<PersonResponseDto> injected = injectPerson();
        assertEquals(3, injected.size());
        PersonResponseDto personToDelete = injected.get(0);
        Long idToDelete = personToDelete.getId();

        mockMvc.perform(delete(ApiVersionPathConstants.V1 + String.format("/person/%d", idToDelete)))
                .andDo(print())
                .andExpect(status().isNoContent())
                .andReturn();

        List<PersonResponseDto> existingPersons = underTest.getAllPersons().getPersons();
        assertEquals(2, existingPersons.size());
        assertFalse(existingPersons.contains(personToDelete));
    }

    @Test
    @Transactional
    @SneakyThrows
    void updatePerson_notFound() {
        PersonAddDto dto = new PersonAddDto(
                "test",
                "test"
        );
        ObjectMapper mapper = new ObjectMapper();
        String jsonObject = mapper.writeValueAsString(dto);

        mockMvc.perform(put(ApiVersionPathConstants.V1 + String.format("/person/%d", 99999L))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonObject))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andReturn();
    }

    @Test
    @Transactional
    @SneakyThrows
    void updatePerson_success() {
        List<PersonResponseDto> injected = injectPerson();

        PersonResponseDto existed = injected.get(0);
        PersonAddDto dto = new PersonAddDto(
                "newFirstName",
                "newLastName"
        );
        PersonResponseDto expected = new PersonResponseDto(
                existed.getId(),
                dto.getFirstName(),
                dto.getLastName(),
                existed.getChannels()
        );

        ObjectMapper mapper = new ObjectMapper();
        String jsonObject = mapper.writeValueAsString(dto);

        MvcResult result = mockMvc.perform(put(ApiVersionPathConstants.V1 + String.format("/person/%d", expected.getId()))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonObject))
                .andDo(print())
                .andExpect(status().is2xxSuccessful())
                .andReturn();

        PersonResponseDto actual = mapper.readValue(result.getResponse().getContentAsString(), PersonResponseDto.class);
        assertEquals(expected, actual);
        List<PersonResponseDto> existingPersons = underTest.getAllPersons().getPersons();
        assertTrue(existingPersons.contains(actual));
        assertFalse(existingPersons.contains(existed));
    }

    static class Initializer implements ApplicationContextInitializer<ConfigurableApplicationContext> {
        public void initialize(ConfigurableApplicationContext configurableApplicationContext) {
            TestPropertyValues.of(
                    "spring.datasource.url=" + sqlContainer.getJdbcUrl(),
                    "spring.datasource.username=" + sqlContainer.getUsername(),
                    "spring.datasource.password=" + sqlContainer.getPassword()
            ).applyTo(configurableApplicationContext.getEnvironment());
        }
    }
}
