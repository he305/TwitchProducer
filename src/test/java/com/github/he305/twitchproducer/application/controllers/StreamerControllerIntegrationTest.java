package com.github.he305.twitchproducer.application.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.he305.twitchproducer.application.constants.ApiVersionPathConstants;
import com.github.he305.twitchproducer.application.dto.StreamerBodyDto;
import com.github.he305.twitchproducer.application.dto.StreamerListDto;
import com.github.he305.twitchproducer.common.entities.Streamer;
import org.junit.jupiter.api.Test;
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

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ContextConfiguration(initializers = {StreamerControllerIntegrationTest.Initializer.class})
class StreamerControllerIntegrationTest {

    private static final PostgreSQLContainer sqlContainer;

    static {
        sqlContainer = new PostgreSQLContainer("postgres")
                .withDatabaseName("integration-tests-db")
                .withUsername("sa")
                .withPassword("sa");
        sqlContainer.start();
    }

    private int counter = 0;
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private StreamerController streamerController;

    @Test
    void controllerIsNotNull() {
        assertNotNull(streamerController);
    }

    @Test
    @Transactional
    void addStreamer() throws Exception {
        StreamerBodyDto bodyDto = new StreamerBodyDto("test");
        ObjectMapper objectMapper = new ObjectMapper();
        String jsonObject = objectMapper.writeValueAsString(bodyDto);

        MvcResult result = mockMvc.perform(post(ApiVersionPathConstants.V1 + "streamer")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonObject))
                .andDo(print())
                .andExpect(status().is2xxSuccessful())
                .andReturn();

        String content = result.getResponse().getContentAsString();
        Streamer actual = objectMapper.readValue(content, Streamer.class);
        assertEquals(bodyDto.getNickname(), actual.getNickname());
    }

    @Test
    @Transactional
    void getAll_empty() throws Exception {
        StreamerListDto streamerListDto = new StreamerListDto(Collections.emptyList());
        ObjectMapper mapper = new ObjectMapper();
        MvcResult result = mockMvc.perform(get(ApiVersionPathConstants.V1 + "streamer"))
                .andDo(print())
                .andExpect(status().is2xxSuccessful())
                .andReturn();

        String content = result.getResponse().getContentAsString();
        StreamerListDto list = mapper.readValue(content, StreamerListDto.class);
        assertTrue(list.getStreamers().isEmpty());
    }

    @Transactional
    private List<String> injectSomeData() {
        List<String> nicknames = List.of(
                "test1",
                "test2",
                "test3"
        );

        List<StreamerBodyDto> requests = nicknames.stream().map(StreamerBodyDto::new).collect(Collectors.toList());
        requests.forEach(streamerController::addStreamer);
        return nicknames;
    }

    @Test
    @Transactional
    void addStreamer_existed() throws Exception {
        List<String> nicknames = injectSomeData();
        StreamerBodyDto bodyDto = new StreamerBodyDto(nicknames.get(0));
        ObjectMapper objectMapper = new ObjectMapper();
        String jsonObject = objectMapper.writeValueAsString(bodyDto);
        counter++;

        mockMvc.perform(post(ApiVersionPathConstants.V1 + "streamer")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonObject))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    @Transactional
    void getAll_withResult() throws Exception {
        List<String> nicknames = injectSomeData();
        ObjectMapper mapper = new ObjectMapper();
        int expectedSize = nicknames.size();

        MvcResult result = mockMvc.perform(get(ApiVersionPathConstants.V1 + "streamer"))
                .andDo(print())
                .andExpect(status().is2xxSuccessful())
                .andReturn();

        String content = result.getResponse().getContentAsString();
        StreamerListDto streamers = mapper.readValue(content, StreamerListDto.class);
        assertEquals(expectedSize, streamers.getStreamers().size());
        streamers.getStreamers().forEach(streamer ->
                assertTrue(nicknames.contains(streamer.getNickname())));
    }

    @Test
    @Transactional
    void getByName_existingEntry() throws Exception {
        List<String> nicknames = injectSomeData();

        ObjectMapper mapper = new ObjectMapper();
        String expected = nicknames.get(0);

        MvcResult result = mockMvc.perform(get(String.format(ApiVersionPathConstants.V1 + "streamer/%s", expected)))
                .andDo(print())
                .andExpect(status().is2xxSuccessful())
                .andReturn();

        String content = result.getResponse().getContentAsString();
        Streamer actual = mapper.readValue(content, Streamer.class);
        assertEquals(expected, actual.getNickname());
    }

    @Test
    @Transactional
    void getByName_noResult() throws Exception {
        Streamer expected = new Streamer();
        ObjectMapper mapper = new ObjectMapper();

        MvcResult result = mockMvc.perform(get(ApiVersionPathConstants.V1 + "streamer/test"))
                .andDo(print())
                .andExpect(status().is2xxSuccessful())
                .andReturn();
        String content = result.getResponse().getContentAsString();
        Streamer actual = mapper.readValue(content, Streamer.class);
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