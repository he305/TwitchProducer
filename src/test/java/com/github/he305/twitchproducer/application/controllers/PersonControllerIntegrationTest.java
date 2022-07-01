package com.github.he305.twitchproducer.application.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.he305.twitchproducer.application.constants.ApiVersionPathConstants;
import com.github.he305.twitchproducer.application.dto.PersonDtoListDto;
import com.github.he305.twitchproducer.application.dto.PersonResponseDto;
import com.github.he305.twitchproducer.common.dto.PersonAddDto;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.util.TestPropertyValues;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.transaction.annotation.Transactional;
import org.testcontainers.containers.PostgreSQLContainer;

import java.util.List;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

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

    private List<PersonAddDto> prepareInjectData() {
        return List.of(
                new PersonAddDto("test", "test1"),
                new PersonAddDto("test2", "test3"),
                new PersonAddDto("test4", "test5")
        );
    }

    @Test
    @Transactional
    void getAll_existingData() throws Exception {
        List<PersonAddDto> injectedData = prepareInjectData();
        injectedData.forEach(underTest::addPerson);

        ObjectMapper mapper = new ObjectMapper();
        MvcResult result = mockMvc
                .perform(get(ApiVersionPathConstants.V1 + "/person"))
                .andDo(print())
                .andReturn();

        PersonDtoListDto actual = mapper.readValue(result.getResponse().getContentAsString(), PersonDtoListDto.class);
        List<PersonAddDto> convertedActual = actual.getPersons().stream().map(p -> new PersonAddDto(p.getFirstName(), p.getLastName())).collect(Collectors.toList());
        assertEquals(injectedData, convertedActual);
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
    void getByLastName_success() throws Exception {
        List<PersonAddDto> injectedData = prepareInjectData();
        injectedData.forEach(underTest::addPerson);
        ObjectMapper mapper = new ObjectMapper();

        PersonAddDto expected = injectedData.get(0);
        MvcResult result = mockMvc
                .perform(get(String.format(ApiVersionPathConstants.V1 + "/person/%s", expected.getLastName())))
                .andDo(print())
                .andReturn();

        PersonResponseDto actual = mapper.readValue(result.getResponse().getContentAsString(), PersonResponseDto.class);
        PersonAddDto convertedActual = new PersonAddDto(actual.getFirstName(), actual.getLastName());
        assertEquals(expected, convertedActual);
    }

    @Test
    @Transactional
    void getByLastName_noResult() throws Exception {
        List<PersonAddDto> injectedData = prepareInjectData();
        injectedData.forEach(underTest::addPerson);

        MvcResult result = mockMvc
                .perform(get(String.format(ApiVersionPathConstants.V1 + "/person/%s", "not_exist")))
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
        List<PersonAddDto> injectedData = prepareInjectData();
        injectedData.forEach(underTest::addPerson);

        ObjectMapper mapper = new ObjectMapper();
        PersonAddDto data = injectedData.get(0);
        String jsonObject = mapper.writeValueAsString(data);

        MvcResult result = mockMvc
                .perform(post(ApiVersionPathConstants.V1 + "/person")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonObject))
                .andDo(print())
                .andReturn();

        assertEquals(HttpStatus.BAD_REQUEST.value(), result.getResponse().getStatus());
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
