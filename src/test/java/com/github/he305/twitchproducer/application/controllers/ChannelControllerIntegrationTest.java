package com.github.he305.twitchproducer.application.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.he305.twitchproducer.application.constants.ApiVersionPathConstants;
import com.github.he305.twitchproducer.application.dto.ChannelListDto;
import com.github.he305.twitchproducer.common.dto.ChannelAddDto;
import com.github.he305.twitchproducer.common.dto.ChannelResponseDto;
import com.github.he305.twitchproducer.common.dto.PersonAddDto;
import com.github.he305.twitchproducer.common.entities.Platform;
import lombok.SneakyThrows;
import org.junit.jupiter.api.BeforeAll;
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

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
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
    private ChannelController underTest;

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
        assertNotNull(underTest);
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

    private List<ChannelResponseDto> injectChannels() {
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
        return requests.stream()
                .map(r -> underTest.addChannel(id, r))
                .map(HttpEntity::getBody)
                .collect(Collectors.toList());
    }

    @Test
    @Transactional
    void getPersonChannelByName_notFoundPerson() throws Exception {
        List<ChannelResponseDto> channels = injectChannels();
        Long id = getPersonId();
        Long dataId = 99999L;
        assertNotEquals(id, dataId);
        ChannelResponseDto dataChannel = channels.get(0);
        String dataNickname = dataChannel.getNickname();
        mockMvc.perform(get(ApiVersionPathConstants.V1 + String.format("/person/%d/channel/%s", dataId, dataNickname)))
                .andDo(print())
                .andExpect(status().isNotFound())
                .andReturn();
    }

    @Test
    @Transactional
    void getPersonChannelByName_notFoundNickname() throws Exception {
        List<ChannelResponseDto> channels = injectChannels();
        Long dataId = getPersonId();
        String dataNickname = "1312312";
        channels.forEach(c -> assertNotEquals(dataNickname, c.getNickname()));

        mockMvc.perform(get(ApiVersionPathConstants.V1 + String.format("/person/%d/channel/%s", dataId, dataNickname)))
                .andDo(print())
                .andExpect(status().isNotFound())
                .andReturn();
    }

    @Test
    @Transactional
    void getPersonChannelByName_valid() throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();
        List<ChannelResponseDto> channels = injectChannels();
        Long dataId = getPersonId();
        ChannelResponseDto dataChannel = channels.get(0);
        String dataNickname = dataChannel.getNickname();

        MvcResult result = mockMvc.perform(get(ApiVersionPathConstants.V1 + String.format("/person/%d/channel/%s", dataId, dataNickname)))
                .andDo(print())
                .andExpect(status().is2xxSuccessful())
                .andReturn();

        String content = result.getResponse().getContentAsString();
        ChannelResponseDto actual = objectMapper.readValue(content, ChannelResponseDto.class);
        assertEquals(dataChannel, actual);
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
        List<ChannelResponseDto> channels = injectChannels();
        ChannelAddDto data = new ChannelAddDto(channels.get(0).getNickname(), channels.get(0).getPlatform());
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
        injectPersonData();
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
        ChannelAddDto data = new ChannelAddDto("test", Platform.TWITCH);
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
        List<ChannelResponseDto> channels = injectChannels();
        ObjectMapper mapper = new ObjectMapper();
        int expectedSize = channels.size();

        MvcResult result = mockMvc.perform(get(ApiVersionPathConstants.V1 + "/channel"))
                .andDo(print())
                .andExpect(status().is2xxSuccessful())
                .andReturn();

        String content = result.getResponse().getContentAsString();
        List<ChannelResponseDto> channelList = mapper.readValue(content, ChannelListDto.class).getChannels();
        assertEquals(expectedSize, channelList.size());
        channelList.forEach(channel ->
                assertTrue(channels.contains(channel)));
    }

    @Test
    @Transactional
    void getByName_existingEntry() throws Exception {
        List<ChannelResponseDto> channels = injectChannels();

        ObjectMapper mapper = new ObjectMapper();
        ChannelResponseDto expected = channels.get(0);
        String nickName = channels.get(0).getNickname();

        MvcResult result = mockMvc.perform(get(String.format(ApiVersionPathConstants.V1 + "/channel/name/%s", nickName)))
                .andDo(print())
                .andExpect(status().is2xxSuccessful())
                .andReturn();

        String content = result.getResponse().getContentAsString();
        ChannelResponseDto actual = mapper.readValue(content, ChannelResponseDto.class);
        assertEquals(expected, actual);
    }

    @Test
    @Transactional
    void getByName_noResult() throws Exception {
        mockMvc.perform(get(ApiVersionPathConstants.V1 + "/channel/name/test"))
                .andDo(print())
                .andExpect(status().isNotFound())
                .andReturn();
    }

    @Test
    @Transactional
    @SneakyThrows
    void deleteChannel_notFound() {
        mockMvc.perform(delete(ApiVersionPathConstants.V1 + String.format("/channel/%d", 99999L)))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andReturn();
    }

    @Test
    @Transactional
    @SneakyThrows
    void deleteChannel_success() {
        List<ChannelResponseDto> injected = injectChannels();
        assertEquals(3, injected.size());
        ChannelResponseDto channelToDelete = injected.get(0);
        Long idToDelete = channelToDelete.getId();

        mockMvc.perform(delete(ApiVersionPathConstants.V1 + String.format("/channel/%d", idToDelete)))
                .andDo(print())
                .andExpect(status().isNoContent())
                .andReturn();

        List<ChannelResponseDto> existingChannels = underTest.getAll().getChannels();
        assertEquals(2, existingChannels.size());
        assertFalse(existingChannels.contains(channelToDelete));
    }

    @Test
    @Transactional
    @SneakyThrows
    void updateChannel_notFound() {
        ChannelAddDto dto = new ChannelAddDto(
                "test",
                Platform.GOODGAME
        );
        ObjectMapper mapper = new ObjectMapper();
        String jsonObject = mapper.writeValueAsString(dto);

        mockMvc.perform(put(ApiVersionPathConstants.V1 + String.format("/channel/%d", 99999L))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonObject))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andReturn();
    }

    @Test
    @Transactional
    @SneakyThrows
    void updateChannel_success() {
        List<ChannelResponseDto> injected = injectChannels();

        ChannelResponseDto existed = injected.get(0);
        ChannelAddDto dto = new ChannelAddDto(
                "newNickname",
                Platform.GOODGAME
        );
        ChannelResponseDto expected = new ChannelResponseDto(
                existed.getId(),
                dto.getNickname(),
                dto.getPlatform(),
                existed.getPersonFullName()
        );

        ObjectMapper mapper = new ObjectMapper();
        String jsonObject = mapper.writeValueAsString(dto);

        MvcResult result = mockMvc.perform(put(ApiVersionPathConstants.V1 + String.format("/channel/%d", expected.getId()))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonObject))
                .andDo(print())
                .andExpect(status().is2xxSuccessful())
                .andReturn();

        ChannelResponseDto actual = mapper.readValue(result.getResponse().getContentAsString(), ChannelResponseDto.class);
        assertEquals(expected, actual);
        List<ChannelResponseDto> existingChannels = underTest.getAll().getChannels();
        assertTrue(existingChannels.contains(actual));
        assertFalse(existingChannels.contains(existed));
    }


    @Test
    @Transactional
    @SneakyThrows
    void getChannelById_notFound() {
        mockMvc.perform(get(ApiVersionPathConstants.V1 + String.format("/channel/id/%d", 0L)))
                .andDo(print())
                .andExpect(status().isNotFound())
                .andReturn();
    }

    @Test
    @Transactional
    @SneakyThrows
    void getChannelId_found() {
        List<ChannelResponseDto> channels = injectChannels();
        ChannelResponseDto expected = channels.get(0);

        ObjectMapper mapper = new ObjectMapper();

        MvcResult result = mockMvc.perform(get(ApiVersionPathConstants.V1 + String.format("/channel/id/%d", expected.getId())))
                .andDo(print())
                .andExpect(status().is2xxSuccessful())
                .andReturn();

        String content = result.getResponse().getContentAsString();
        ChannelResponseDto actual = mapper.readValue(content, ChannelResponseDto.class);
        assertEquals(expected, actual);
    }

    @Test
    @Transactional
    @SneakyThrows
    void getChannelById_notFoundWithData() {
        injectChannels();
        mockMvc.perform(get(ApiVersionPathConstants.V1 + String.format("/channel/id/%d", 99999L)))
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