package com.github.he305.twitchproducer.application.mapper;

import com.github.he305.twitchproducer.application.dto.PersonResponseDto;
import com.github.he305.twitchproducer.common.dto.ChannelResponseDto;
import com.github.he305.twitchproducer.common.entities.Channel;
import com.github.he305.twitchproducer.common.entities.Person;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(MockitoExtension.class)
class PersonResponseMapperImplTest {

    @Mock
    private ChannelResponseMapper mapper;
    private PersonResponseMapperImpl underTest;

    @BeforeEach
    public void setUp() {
        underTest = new PersonResponseMapperImpl(mapper);
    }

    @Test
    void getPersonDto() {
        ChannelResponseDto mockChannel = new ChannelResponseDto();
        Mockito.when(mapper.toDto(Mockito.any())).thenReturn(mockChannel);
        Person data = new Person(0L, "test1", "test2", List.of(new Channel()));
        PersonResponseDto expected = new PersonResponseDto(0L, "test1", "test2", List.of(mockChannel));

        PersonResponseDto actual = underTest.getPersonDto(data);
        assertEquals(expected, actual);
    }
}