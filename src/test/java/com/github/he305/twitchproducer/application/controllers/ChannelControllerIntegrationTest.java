package com.github.he305.twitchproducer.application.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.he305.twitchproducer.application.constants.ApiVersionPathConstants;
import com.github.he305.twitchproducer.application.dto.ChannelListDto;
import com.github.he305.twitchproducer.common.dto.ChannelAddDto;
import com.github.he305.twitchproducer.common.dto.ChannelResponseDto;
import com.github.he305.twitchproducer.common.dto.PersonAddDto;
import com.github.he305.twitchproducer.common.entities.Platform;
import org.junit.jupiter.api.BeforeAll;
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
@ContextConfiguration(initializers = {ChannelControllerIntegrationTest.Initializer.class})
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class ChannelControllerIntegrationTest {

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
    private PersonController personController;
    @Autowired
    private ChannelController channelController;

    @BeforeAll
    void setUp() {
        //injectPersonData();
    }

    void injectPersonData() {
        PersonAddDto personResponseDto = new PersonAddDto("testName", "testLastName");
        personController.addPerson(personResponseDto);
    }

    Long getPersonId() {
        return personController.getAllPersons().getPersons().get(0).getId();
    }

    @Test
    void controllerIsNotNull() {
        assertNotNull(channelController);
    }

    @Test
    @Transactional
    void getAll_empty() throws Exception {
        ChannelListDto channelListDto = new ChannelListDto(Collections.emptyList());
        ObjectMapper mapper = new ObjectMapper();
        MvcResult result = mockMvc.perform(get(ApiVersionPathConstants.V1 + "/channel"))
                .andDo(print())
                .andExpect(status().is2xxSuccessful())
                .andReturn();

        String content = result.getResponse().getContentAsString();
        ChannelListDto list = mapper.readValue(content, ChannelListDto.class);
        assertTrue(list.getChannels().isEmpty());
    }

    private List<String> injectSomeData() {
        injectPersonData();
        List<String> nicknames = List.of(
                "test1",
                "test2",
                "test3"
        );

        List<ChannelAddDto> requests = nicknames.stream()
                .map(nick -> new ChannelAddDto(nick, Platform.TWITCH))
                .collect(Collectors.toList());
        Long id = personController.getAllPersons().getPersons().get(0).getId();
        requests.forEach(r -> channelController.addChannel(id, r));
        return nicknames;
    }

    @Test
    @Transactional
    void getPersonChannelByName_notFoundPerson() throws Exception {
        List<String> nicknames = injectSomeData();
        Long id = getPersonId();
        Long dataId = 99999L;
        assertNotEquals(id, dataId);
        String dataNickname = nicknames.get(0);
        assertTrue(nicknames.contains(dataNickname));

        mockMvc.perform(get(ApiVersionPathConstants.V1 + String.format("/person/%d/channel/%s", dataId, dataNickname)))
                .andDo(print())
                .andExpect(status().isNotFound())
                .andReturn();
    }

    @Test
    @Transactional
    void getPersonChannelByName_notFoundNickname() throws Exception {
        List<String> nicknames = injectSomeData();
        Long dataId = getPersonId();
        String dataNickname = "1312312";
        assertFalse(nicknames.contains(dataNickname));

        mockMvc.perform(get(ApiVersionPathConstants.V1 + String.format("/person/%d/channel/%s", dataId, dataNickname)))
                .andDo(print())
                .andExpect(status().isNotFound())
                .andReturn();
    }

    @Test
    @Transactional
    void getPersonChannelByName_valid() throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();
        List<String> nicknames = injectSomeData();
        Long dataId = getPersonId();
        String dataNickname = nicknames.get(0);

        MvcResult result = mockMvc.perform(get(ApiVersionPathConstants.V1 + String.format("/person/%d/channel/%s", dataId, dataNickname)))
                .andDo(print())
                .andExpect(status().is2xxSuccessful())
                .andReturn();

        String content = result.getResponse().getContentAsString();
        ChannelResponseDto actual = objectMapper.readValue(content, ChannelResponseDto.class);
        assertEquals(dataNickname, actual.getNickname());
    }

    @Test
    @Transactional
    void addChannel_valid() throws Exception {
        ChannelAddDto bodyDto = new ChannelAddDto("test", Platform.TWITCH);
        ObjectMapper objectMapper = new ObjectMapper();
        String jsonObject = objectMapper.writeValueAsString(bodyDto);

        injectPersonData();
        Long id = getPersonId();
        MvcResult result = mockMvc.perform(post(ApiVersionPathConstants.V1 + String.format("/person/%d/channel", id))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonObject))
                .andDo(print())
                .andExpect(status().is2xxSuccessful())
                .andReturn();

        String content = result.getResponse().getContentAsString();
        ChannelResponseDto actual = objectMapper.readValue(content, ChannelResponseDto.class);
        assertEquals(bodyDto.getNickname(), actual.getNickname());
    }

    @Test
    @Transactional
    void addChannel_existingChannel() throws Exception {
        List<String> nicknames = injectSomeData();
        ChannelAddDto data = new ChannelAddDto(nicknames.get(0), Platform.TWITCH);
        ObjectMapper objectMapper = new ObjectMapper();
        String jsonObject = objectMapper.writeValueAsString(data);

        Long id = getPersonId();
        mockMvc.perform(post(ApiVersionPathConstants.V1 + String.format("/person/%d/channel", id))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonObject))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    @Transactional
    void addChannel_emptyNickname() throws Exception {
        List<String> nicknames = injectSomeData();
        ChannelAddDto data = new ChannelAddDto("", Platform.TWITCH);
        ObjectMapper objectMapper = new ObjectMapper();
        String jsonObject = objectMapper.writeValueAsString(data);

        Long id = getPersonId();
        mockMvc.perform(post(ApiVersionPathConstants.V1 + String.format("/person/%d/channel", id))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonObject))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    @Transactional
    void addChannel_notFoundPerson() throws Exception {
        List<String> nicknames = injectSomeData();
        ChannelAddDto data = new ChannelAddDto(nicknames.get(0), Platform.TWITCH);
        ObjectMapper objectMapper = new ObjectMapper();
        String jsonObject = objectMapper.writeValueAsString(data);

        mockMvc.perform(post(ApiVersionPathConstants.V1 + String.format("/person/%d/channel", 999999L))
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

        MvcResult result = mockMvc.perform(get(ApiVersionPathConstants.V1 + "/channel"))
                .andDo(print())
                .andExpect(status().is2xxSuccessful())
                .andReturn();

        String content = result.getResponse().getContentAsString();
        ChannelListDto channels = mapper.readValue(content, ChannelListDto.class);
        assertEquals(expectedSize, channels.getChannels().size());
        channels.getChannels().forEach(channel ->
                assertTrue(nicknames.contains(channel.getNickname())));
    }

    @Test
    @Transactional
    void getByName_existingEntry() throws Exception {
        List<String> nicknames = injectSomeData();

        ObjectMapper mapper = new ObjectMapper();
        String expected = nicknames.get(0);

        MvcResult result = mockMvc.perform(get(String.format(ApiVersionPathConstants.V1 + "/channel/%s", expected)))
                .andDo(print())
                .andExpect(status().is2xxSuccessful())
                .andReturn();

        String content = result.getResponse().getContentAsString();
        ChannelResponseDto actual = mapper.readValue(content, ChannelResponseDto.class);
        assertEquals(expected, actual.getNickname());
    }

    @Test
    @Transactional
    void getByName_noResult() throws Exception {
        ChannelResponseDto expected = new ChannelResponseDto();
        ObjectMapper mapper = new ObjectMapper();

        MvcResult result = mockMvc.perform(get(ApiVersionPathConstants.V1 + "/channel/test"))
                .andDo(print())
                .andExpect(status().is2xxSuccessful())
                .andReturn();
        String content = result.getResponse().getContentAsString();
        ChannelResponseDto actual = mapper.readValue(content, ChannelResponseDto.class);
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