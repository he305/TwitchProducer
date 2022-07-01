package com.github.he305.twitchproducer.application.mapper;

import com.github.he305.twitchproducer.common.dto.PersonResponseDto;
import com.github.he305.twitchproducer.common.entities.Person;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;

import static org.junit.jupiter.api.Assertions.assertEquals;

class PersonResponseMapperImplTest {

    private PersonResponseMapperImpl underTest;

    @BeforeEach
    public void setUp() {
        underTest = new PersonResponseMapperImpl(new ModelMapper());
    }

    @Test
    void getPersonDto() {
        Person data = new Person(0L, "test1", "test2", null);
        PersonResponseDto expected = new PersonResponseDto(0L, "test1", "test2");

        PersonResponseDto actual = underTest.getPersonDto(data);
        assertEquals(expected, actual);
    }
}