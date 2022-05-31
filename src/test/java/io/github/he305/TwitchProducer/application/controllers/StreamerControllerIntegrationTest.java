package io.github.he305.TwitchProducer.application.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.he305.TwitchProducer.application.dto.StreamerBodyDto;
import io.github.he305.TwitchProducer.common.entities.Streamer;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.util.TestPropertyValues;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import org.testcontainers.containers.PostgreSQLContainer;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@ActiveProfiles("test")
@ContextConfiguration(initializers = {StreamerControllerIntegrationTest.Initializer.class})
class StreamerControllerIntegrationTest {

    private static final PostgreSQLContainer sqlContainer;

    static {
        sqlContainer = new PostgreSQLContainer("postgres:10.7")
                .withDatabaseName("integration-tests-db")
                .withUsername("sa")
                .withPassword("sa");
        sqlContainer.start();
    }

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private StreamerController streamerController;

    @Test
    public void controllerIsNotNull() {
        assertNotNull(streamerController);
    }

    @Test
    @Transactional
    public void addStreamer() throws Exception {
        StreamerBodyDto bodyDto = new StreamerBodyDto("test");
        ObjectMapper objectMapper = new ObjectMapper();
        String jsonObject = objectMapper.writeValueAsString(bodyDto);

        Streamer expected = new Streamer(1l, "test");
        String expectedJson = objectMapper.writeValueAsString(expected);

        mockMvc.perform(post("/api/v1/streamer/add")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonObject))
                .andDo(print())
                .andExpect(status().is2xxSuccessful())
                .andExpect(content().string(expectedJson));
    }

    @Test
    @Transactional
    public void getAll_empty() throws Exception {
        mockMvc.perform(get("/api/v1/streamer/all"))
                .andDo(print())
                .andExpect(status().is2xxSuccessful())
                .andExpect(content().string("[]"));
    }

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
    public void getAll_withResult() throws Exception {
        List<String> nicknames = injectSomeData();

        AtomicInteger counter = new AtomicInteger(0);
        List<Streamer> expectedStreamers = nicknames.stream().map(nickname -> {
            counter.incrementAndGet();
            return new Streamer(counter.longValue(), nickname);
        }).collect(Collectors.toList());
        ObjectMapper mapper = new ObjectMapper();
        String expected = mapper.writeValueAsString(expectedStreamers);

        mockMvc.perform(get("/api/v1/streamer/all"))
                .andDo(print())
                .andExpect(status().is2xxSuccessful())
                .andExpect(content().string(expected));
    }

    @Test
    @Transactional
    public void getByName_existingEntry() throws Exception {
        List<String> nicknames = injectSomeData();

        AtomicInteger counter = new AtomicInteger(0);
        List<Streamer> expectedStreamers = nicknames.stream().map(nickname -> {
            counter.incrementAndGet();
            return new Streamer(counter.longValue(), nickname);
        }).collect(Collectors.toList());
        ObjectMapper mapper = new ObjectMapper();
        String expected = mapper.writeValueAsString(expectedStreamers.get(0));

        mockMvc.perform(get(String.format("/api/v1/streamer/%s", nicknames.get(0))))
                .andDo(print())
                .andExpect(status().is2xxSuccessful())
                .andExpect(content().string(expected));
    }

    @Test
    public void getByName_noResult() throws Exception {
        Streamer expectedStreamer = new Streamer();
        ObjectMapper mapper = new ObjectMapper();
        String expected = mapper.writeValueAsString(expectedStreamer);

        mockMvc.perform(get("/api/v1/streamer/test"))
                .andDo(print())
                .andExpect(status().is2xxSuccessful())
                .andExpect(content().string(expected));
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