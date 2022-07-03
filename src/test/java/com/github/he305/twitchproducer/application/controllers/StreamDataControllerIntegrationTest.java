package com.github.he305.twitchproducer.application.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.github.he305.twitchproducer.application.constants.ApiVersionPathConstants;
import com.github.he305.twitchproducer.application.dto.StreamDataList;
import com.github.he305.twitchproducer.application.dto.StreamEndRequest;
import com.github.he305.twitchproducer.common.dto.*;
import com.github.he305.twitchproducer.common.entities.Platform;
import lombok.SneakyThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.util.TestPropertyValues;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.http.HttpEntity;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.transaction.annotation.Transactional;
import org.testcontainers.containers.PostgreSQLContainer;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ContextConfiguration(initializers = {StreamDataControllerIntegrationTest.Initializer.class})
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class StreamDataControllerIntegrationTest {

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
    private StreamController streamController;
    @Autowired
    private ChannelController channelController;
    @Autowired
    private PersonController personController;

    @Autowired
    private StreamDataController underTest;

    private ObjectMapper mapper;

    @BeforeEach
    void setUp() {
        mapper = JsonMapper.builder()
                .findAndAddModules()
                .build();
    }

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

    void injectStream() {
        injectChannel();
        StreamAddDto stream = new StreamAddDto(LocalDateTime.now());
        streamController.addStream(getChannelId(), stream);
    }

    Long getStreamId() {
        return streamController.getAllStreams().getStreams().get(0).getId();
    }

    List<StreamDataResponseDto> injectStreamData() {
        injectStream();
        Long streamId = getStreamId();
        LocalDateTime time = LocalDateTime.now();
        List<StreamDataAddDto> dto = List.of(
                new StreamDataAddDto("test", "title", 0, time),
                new StreamDataAddDto("test", "title", 0, time.minusHours(1)),
                new StreamDataAddDto("test", "title", 0, time.minusHours(2))
        );

        return dto
                .stream()
                .map(d -> underTest.addStreamData(streamId, d))
                .map(HttpEntity::getBody)
                .collect(Collectors.toList());
    }

    @Test
    @Transactional
    @SneakyThrows
    void getAllStreamsForStreamId_noData() {
        MvcResult result = mockMvc.perform(get(ApiVersionPathConstants.V1 + String.format("/stream/%d/streamData", 9999L)))
                .andDo(print())
                .andExpect(status().is2xxSuccessful())
                .andReturn();

        StreamDataList actual = mapper.readValue(result.getResponse().getContentAsString(), StreamDataList.class);
        assertTrue(actual.getStreamData().isEmpty());
    }

    @Test
    @Transactional
    @SneakyThrows
    void getAllStreamsForStreamId_someData() {
        List<StreamDataResponseDto> expected = injectStreamData();
        Long streamId = getStreamId();
        MvcResult result = mockMvc.perform(get(ApiVersionPathConstants.V1 + String.format("/stream/%d/streamData", streamId)))
                .andDo(print())
                .andExpect(status().is2xxSuccessful())
                .andReturn();

        StreamDataList actual = mapper.readValue(result.getResponse().getContentAsString(), StreamDataList.class);
        assertEquals(expected.size(), actual.getStreamData().size());
        actual.getStreamData().forEach(a ->
                assertTrue(expected.contains(a)));
    }

    @Test
    @Transactional
    @SneakyThrows
    void addStreamData_notFoundStream() {
        StreamDataAddDto dto = new StreamDataAddDto();
        String jsonObject = mapper.writeValueAsString(dto);
        mockMvc.perform(post(ApiVersionPathConstants.V1 + String.format("/stream/%d/streamData", 99999L))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonObject))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andReturn();
    }

    @Test
    @Transactional
    @SneakyThrows
    void addStreamData_streamHasEnded() {
        injectStream();
        Long streamId = getStreamId();
        StreamResponseDto endedStream = streamController.endStream(streamId, new StreamEndRequest(LocalDateTime.now())).getBody();
        assertNotNull(endedStream);
        assertNotNull(endedStream.getEndedAt());

        StreamDataAddDto dto = new StreamDataAddDto();
        String jsonObject = mapper.writeValueAsString(dto);
        mockMvc.perform(post(ApiVersionPathConstants.V1 + String.format("/stream/%d/streamData", streamId))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonObject))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andReturn();
    }

    @Test
    @Transactional
    @SneakyThrows
    void addStreamData_valid() {
        injectStreamData();
        LocalDateTime now = LocalDateTime.now();
        StreamDataAddDto dto = new StreamDataAddDto(
                "test",
                "name",
                0,
                now
        );
        StreamDataResponseDto expected = new StreamDataResponseDto(
                null,
                "test",
                "name",
                0,
                now
        );
        Long streamId = getStreamId();
        String jsonObject = mapper.writeValueAsString(dto);
        MvcResult result = mockMvc.perform(post(ApiVersionPathConstants.V1 + String.format("/stream/%d/streamData", streamId))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonObject))
                .andDo(print())
                .andExpect(status().is2xxSuccessful())
                .andReturn();

        StreamDataResponseDto actual = mapper.readValue(result.getResponse().getContentAsString(), StreamDataResponseDto.class);
        expected.setId(actual.getId());
        assertEquals(expected, actual);
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