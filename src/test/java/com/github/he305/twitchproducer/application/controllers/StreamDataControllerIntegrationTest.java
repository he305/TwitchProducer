package com.github.he305.twitchproducer.application.controllers;

import com.fasterxml.jackson.databind.json.JsonMapper;
import com.github.he305.twitchproducer.application.constants.ApiVersionPathConstants;
import com.github.he305.twitchproducer.application.dto.StreamDataList;
import com.github.he305.twitchproducer.common.dao.ChannelDao;
import com.github.he305.twitchproducer.common.dao.StreamDao;
import com.github.he305.twitchproducer.common.dao.StreamDataDao;
import com.github.he305.twitchproducer.common.dto.ChannelAddDto;
import com.github.he305.twitchproducer.common.dto.PersonAddDto;
import com.github.he305.twitchproducer.common.dto.StreamDataResponseDto;
import com.github.he305.twitchproducer.common.entities.Platform;
import com.github.he305.twitchproducer.common.entities.Stream;
import com.github.he305.twitchproducer.common.entities.StreamData;
import com.github.he305.twitchproducer.common.mapper.StreamDataResponseMapper;
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
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.transaction.annotation.Transactional;
import org.testcontainers.containers.PostgreSQLContainer;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
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
    private ChannelDao channelDao;
    @Autowired
    private StreamDao streamDao;
    @Autowired
    private StreamDataDao streamDataDao;
    @Autowired
    private StreamDataResponseMapper streamDataResponseMapper;

    @Autowired
    private StreamDataController underTest;

    private JsonMapper mapper;

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
        Stream stream = new Stream(
                null,
                LocalDateTime.now(),
                null,
                0,
                Collections.emptyList(),
                channelDao.get(getChannelId()).get()
        );
        streamDao.save(stream);
    }

    Long getStreamId() {
        return streamController.getAllStreams().getStreams().get(0).getId();
    }

    List<StreamData> injectStreamData() {
        injectStream();
        Long channelId = getChannelId();
        LocalDateTime time = LocalDateTime.now();
        Stream stream = streamDao.get(getStreamId()).get();

        List<StreamData> streamDatas = List.of(
                new StreamData(null, "test", "title", 0, stream, time),
                new StreamData(null, "test", "title", 0, stream, time.minusHours(1)),
                new StreamData(null, "test", "title", 1, stream, time.minusHours(2))
        );

        return streamDatas
                .stream()
                .map(s -> streamDataDao.save(s))
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
        List<StreamData> streamDatas = injectStreamData();
        List<StreamDataResponseDto> expected = streamDatas.stream()
                .map(streamDataResponseMapper::toDto)
                .collect(Collectors.toList());

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