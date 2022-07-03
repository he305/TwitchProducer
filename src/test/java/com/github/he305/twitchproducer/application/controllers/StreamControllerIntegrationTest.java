package com.github.he305.twitchproducer.application.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.github.he305.twitchproducer.application.constants.ApiVersionPathConstants;
import com.github.he305.twitchproducer.application.dto.StreamEndRequest;
import com.github.he305.twitchproducer.application.dto.StreamListDto;
import com.github.he305.twitchproducer.common.dto.ChannelAddDto;
import com.github.he305.twitchproducer.common.dto.PersonAddDto;
import com.github.he305.twitchproducer.common.dto.StreamAddDto;
import com.github.he305.twitchproducer.common.dto.StreamResponseDto;
import com.github.he305.twitchproducer.common.entities.Platform;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.util.TestPropertyValues;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.http.HttpEntity;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.transaction.annotation.Transactional;
import org.testcontainers.containers.PostgreSQLContainer;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ContextConfiguration(initializers = {StreamControllerIntegrationTest.Initializer.class})
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class StreamControllerIntegrationTest {
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
    private StreamController underTest;
    @Autowired
    private ChannelController channelController;
    @Autowired
    private PersonController personController;

    void injectPersonData() {
        PersonAddDto personResponseDto = new PersonAddDto("testName", "testLastName");
        personController.addPerson(personResponseDto);
    }

    Long getPersonId() {
        return personController.getAllPersons().getPersons().get(0).getId();
    }

    void injectChannel() {
        injectPersonData();
        ChannelAddDto channel = new ChannelAddDto("test", Platform.TWITCH);
        channelController.addChannel(getPersonId(), channel);
    }

    Long getChannelId() {
        return channelController.getAll().getChannels().get(0).getId();
    }

    List<StreamResponseDto> injectStreamData() {
        injectChannel();
        LocalDateTime now = LocalDateTime.now();
        List<StreamAddDto> streams = List.of(
                new StreamAddDto(now),
                new StreamAddDto(now.minusHours(1)),
                new StreamAddDto(now.minusHours(2))
        );
        List<StreamResponseDto> savedStreams = streams
                .stream()
                .map(s -> underTest.addStream(getChannelId(), s))
                .map(HttpEntity::getBody)
                .collect(Collectors.toList());
        underTest.endStream(savedStreams.get(savedStreams.size() - 1).getId(), new StreamEndRequest(now));
        return savedStreams;
    }

    @Test
    @Transactional
    @SneakyThrows
    void getAll_noData() {
        ObjectMapper mapper = JsonMapper.builder()
                .findAndAddModules()
                .build();
        MvcResult result = mockMvc.perform(get(ApiVersionPathConstants.V1 + "/stream"))
                .andDo(print())
                .andExpect(status().is2xxSuccessful())
                .andReturn();

        String content = result.getResponse().getContentAsString();
        StreamListDto list = mapper.readValue(content, StreamListDto.class);
        assertTrue(list.getStreams().isEmpty());
    }

    @Test
    @Transactional
    @SneakyThrows
    void getAll_withResult() {
        List<StreamResponseDto> streams = injectStreamData();
        ObjectMapper mapper = JsonMapper.builder()
                .findAndAddModules()
                .build();
        int expectedSize = streams.size();

        MvcResult result = mockMvc.perform(get(ApiVersionPathConstants.V1 + "/stream"))
                .andDo(print())
                .andExpect(status().is2xxSuccessful())
                .andReturn();

        String content = result.getResponse().getContentAsString();
        StreamListDto actual = mapper.readValue(content, StreamListDto.class);
        assertEquals(expectedSize, actual.getStreams().size());
        actual.getStreams().forEach(a ->
                assertEquals(1, streams
                        .stream()
                        .filter(s -> s.getStartedAt().equals(a.getStartedAt()))
                        .count()));
    }

    @Test
    @Transactional
    @SneakyThrows
    void getCurrent_noData() {
        ObjectMapper mapper = JsonMapper.builder()
                .findAndAddModules()
                .build();
        MvcResult result = mockMvc.perform(get(ApiVersionPathConstants.V1 + "/stream/current"))
                .andDo(print())
                .andExpect(status().is2xxSuccessful())
                .andReturn();

        String content = result.getResponse().getContentAsString();
        StreamListDto list = mapper.readValue(content, StreamListDto.class);
        assertTrue(list.getStreams().isEmpty());
    }

    @Test
    @Transactional
    @SneakyThrows
    void getCurrent_withResult() {
        List<StreamResponseDto> streams = injectStreamData();
        ObjectMapper mapper = JsonMapper.builder()
                .findAndAddModules()
                .build();
        int expectedSize = 2;

        MvcResult result = mockMvc.perform(get(ApiVersionPathConstants.V1 + "/stream/current"))
                .andDo(print())
                .andExpect(status().is2xxSuccessful())
                .andReturn();

        String content = result.getResponse().getContentAsString();
        StreamListDto actual = mapper.readValue(content, StreamListDto.class);
        assertEquals(expectedSize, actual.getStreams().size());
    }

    @Test
    @Transactional
    @SneakyThrows
    void getStreamById_notFound() {
        mockMvc.perform(get(ApiVersionPathConstants.V1 + String.format("/stream/%d", 0L)))
                .andDo(print())
                .andExpect(status().isNotFound())
                .andReturn();
    }

    @Test
    @Transactional
    @SneakyThrows
    void getStreamById_found() {
        List<StreamResponseDto> streams = injectStreamData();
        StreamResponseDto expected = streams.get(0);
        ObjectMapper mapper = JsonMapper.builder()
                .findAndAddModules()
                .build();
        MvcResult result = mockMvc.perform(get(ApiVersionPathConstants.V1 + String.format("/stream/%d", expected.getId())))
                .andDo(print())
                .andExpect(status().is2xxSuccessful())
                .andReturn();

        String content = result.getResponse().getContentAsString();
        StreamResponseDto actual = mapper.readValue(content, StreamResponseDto.class);
        assertEquals(expected, actual);
    }

    @Test
    @Transactional
    @SneakyThrows
    void getStreamById_notFoundWithData() {
        List<StreamResponseDto> streams = injectStreamData();
        mockMvc.perform(get(ApiVersionPathConstants.V1 + String.format("/stream/%d", 99999L)))
                .andDo(print())
                .andExpect(status().isNotFound())
                .andReturn();
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
