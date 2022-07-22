package com.github.he305.twitchproducer.application.controllers;

import com.fasterxml.jackson.databind.json.JsonMapper;
import com.github.he305.twitchproducer.application.constants.ApiVersionPathConstants;
import com.github.he305.twitchproducer.application.dto.StreamEndRequest;
import com.github.he305.twitchproducer.application.dto.StreamListDto;
import com.github.he305.twitchproducer.common.dao.ChannelDao;
import com.github.he305.twitchproducer.common.dao.StreamDao;
import com.github.he305.twitchproducer.common.dto.*;
import com.github.he305.twitchproducer.common.entities.Channel;
import com.github.he305.twitchproducer.common.entities.Platform;
import com.github.he305.twitchproducer.common.entities.Stream;
import com.github.he305.twitchproducer.common.mapper.StreamResponseMapper;
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
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.transaction.annotation.Transactional;
import org.testcontainers.containers.PostgreSQLContainer;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
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
    private StreamDao streamDao;
    @Autowired
    private ChannelDao channelDao;

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private StreamController underTest;
    @Autowired
    private ChannelController channelController;
    @Autowired
    private PersonController personController;
    @Autowired
    private StreamResponseMapper streamResponseMapper;

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

    @SuppressWarnings("OptionalGetWithoutIsPresent")
    List<Stream> injectStream() {
        injectChannel();
        Channel channel = channelDao.get(getChannelId()).get();
        LocalDateTime now = LocalDateTime.now();
        List<Stream> streams = List.of(
                new Stream(null, now, now.minusHours(1), 0, new ArrayList<>(), channel),
                new Stream(null, now.minusHours(1), now, 0, new ArrayList<>(), channel),
                new Stream(null, now.minusHours(2), null, 0, new ArrayList<>(), channel)
        );

        channel.setIsLive(true);

        return streams
                .stream()
                .map(s -> streamDao.save(s))
                .collect(Collectors.toList());
    }

    @Test
    @Transactional
    @SneakyThrows
    void getAll_noData() {
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
        List<Stream> streams = injectStream();
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
        List<Stream> streams = injectStream();
        int expectedSize = 1;

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
        List<Stream> streams = injectStream();
        StreamResponseDto expected = streamResponseMapper.toDto(streams.get(0));

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
        List<Stream> streams = injectStream();
        mockMvc.perform(get(ApiVersionPathConstants.V1 + String.format("/stream/%d", 99999L)))
                .andDo(print())
                .andExpect(status().isNotFound())
                .andReturn();
    }

    @Test
    @Transactional
    @SneakyThrows
    void addStream_notFoundChannel() {
        StreamDataAddDto dto = new StreamDataAddDto(
                "",
                "",
                0,
                LocalDateTime.now()
        );

        String jsonObject = mapper.writeValueAsString(dto);

        mockMvc.perform(post(ApiVersionPathConstants.V1 + String.format("/channel/%d/streamData", 99999L))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonObject))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andReturn();
    }

    @Test
    @Transactional
    @SneakyThrows
    void addStream_existingStream_valid() {
        List<Stream> injectedStreams = injectStream();
        Stream onlineStream = injectedStreams.stream().filter(stream -> stream.getEndedAt() == null).findFirst().get();
        LocalDateTime time = LocalDateTime.now();
        StreamDataAddDto dto = new StreamDataAddDto(
                "test",
                "title",
                0,
                time
        );
        String jsonObject = mapper.writeValueAsString(dto);
        Long channelId = getChannelId();

        StreamResponseDto expected = new StreamResponseDto(
                onlineStream.getId(),
                onlineStream.getStartedAt(),
                null,
                0,
                channelId
        );

        List<ChannelResponseDto> liveChannels = channelController.getLiveChannels().getChannels();
        assertEquals(1, liveChannels.size());
        assertEquals(channelId, liveChannels.get(0).getId());

        MvcResult result = mockMvc.perform(post(ApiVersionPathConstants.V1 + String.format("/channel/%d/streamData", channelId))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonObject))
                .andDo(print())
                .andExpect(status().is2xxSuccessful())
                .andReturn();

        String content = result.getResponse().getContentAsString();
        StreamResponseDto actual = mapper.readValue(content, StreamResponseDto.class);
        expected.setId(actual.getId());
        assertEquals(expected, actual);

        liveChannels = channelController.getLiveChannels().getChannels();
        assertEquals(1, liveChannels.size());
        assertEquals(channelId, liveChannels.get(0).getId());
    }

    @Test
    @Transactional
    @SneakyThrows
    void addStream_newStream_valid() {
        injectChannel();
        LocalDateTime time = LocalDateTime.now();
        StreamDataAddDto dto = new StreamDataAddDto(
                "test",
                "title",
                0,
                time
        );
        String jsonObject = mapper.writeValueAsString(dto);
        Long channelId = getChannelId();

        StreamResponseDto expected = new StreamResponseDto(
                null,
                time,
                null,
                0,
                getChannelId()
        );

        List<ChannelResponseDto> liveChannels = channelController.getLiveChannels().getChannels();
        assertTrue(liveChannels.isEmpty());
        MvcResult result = mockMvc.perform(post(ApiVersionPathConstants.V1 + String.format("/channel/%d/streamData", channelId))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonObject))
                .andDo(print())
                .andExpect(status().is2xxSuccessful())
                .andReturn();

        String content = result.getResponse().getContentAsString();
        StreamResponseDto actual = mapper.readValue(content, StreamResponseDto.class);
        expected.setId(actual.getId());
        assertEquals(expected, actual);
        liveChannels = channelController.getLiveChannels().getChannels();
        assertEquals(1, liveChannels.size());
        assertEquals(channelId, liveChannels.get(0).getId());
    }

    @Test
    @Transactional
    @SneakyThrows
    void addStream_noChannel() {
        LocalDateTime time = LocalDateTime.now();
        StreamDataAddDto dto = new StreamDataAddDto(
                "test",
                "title",
                0,
                time
        );
        String jsonObject = mapper.writeValueAsString(dto);
        Long emptyChannelId = 99999L;

        mockMvc.perform(post(ApiVersionPathConstants.V1 + String.format("/channel/%d/streamData", emptyChannelId))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonObject))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    @Transactional
    @SneakyThrows
    void endStream_existingStream_valid() {
        List<Stream> injectedStreams = injectStream();
        Stream onlineStream = injectedStreams.stream().filter(stream -> stream.getEndedAt() == null).findFirst().get();
        LocalDateTime endTime = LocalDateTime.now().plusHours(1);
        StreamEndRequest endRequest = new StreamEndRequest(
                endTime
        );
        String jsonObject = mapper.writeValueAsString(endRequest);
        Long channelId = getChannelId();

        StreamResponseDto expected = new StreamResponseDto(
                onlineStream.getId(),
                onlineStream.getStartedAt(),
                endTime,
                0,
                getChannelId()
        );

        List<ChannelResponseDto> liveChannels = channelController.getLiveChannels().getChannels();
        assertEquals(1, liveChannels.size());
        assertEquals(channelId, liveChannels.get(0).getId());
        MvcResult result = mockMvc.perform(put(ApiVersionPathConstants.V1 + String.format("/channel/%d/end", channelId))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonObject))
                .andDo(print())
                .andExpect(status().is2xxSuccessful())
                .andReturn();

        String content = result.getResponse().getContentAsString();
        StreamResponseDto actual = mapper.readValue(content, StreamResponseDto.class);
        assertNotNull(actual.getEndedAt());
        expected.setId(actual.getId());
        assertEquals(expected, actual);
        liveChannels = channelController.getLiveChannels().getChannels();
        assertTrue(liveChannels.isEmpty());
    }

    @Test
    @Transactional
    @SneakyThrows
    void endStream_noLiveStream() {
        injectChannel();
        LocalDateTime endTime = LocalDateTime.now().plusHours(1);
        StreamEndRequest endRequest = new StreamEndRequest(
                endTime
        );
        String jsonObject = mapper.writeValueAsString(endRequest);
        Long channelId = getChannelId();

        mockMvc.perform(put(ApiVersionPathConstants.V1 + String.format("/channel/%d/end", channelId))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonObject))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    @Transactional
    @SneakyThrows
    void endStream_noChannel() {
        LocalDateTime time = LocalDateTime.now();
        StreamDataAddDto dto = new StreamDataAddDto(
                "test",
                "title",
                0,
                time
        );
        String jsonObject = mapper.writeValueAsString(dto);
        Long emptyChannelId = 99999L;

        mockMvc.perform(put(ApiVersionPathConstants.V1 + String.format("/channel/%d/end", emptyChannelId))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonObject))
                .andDo(print())
                .andExpect(status().isBadRequest());
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
