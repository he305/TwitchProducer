package com.github.he305.twitchproducer.application.mapper;

import com.github.he305.twitchproducer.common.dto.PersonDto;
import com.github.he305.twitchproducer.common.entities.Person;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;

import static org.junit.jupiter.api.Assertions.assertEquals;

class PersonMapperImplTest {

    private PersonMapperImpl underTest;

    @BeforeEach
    public void setUp() {
        underTest = new PersonMapperImpl(new ModelMapper());
    }

    @Test
    void getPerson() {
        PersonDto data = new PersonDto("test1", "test2");
        Person expected = new Person(null, "test1", "test2", null);

        Person actual = underTest.getPerson(data);
        assertEquals(expected, actual);
    }

    @Test
    void getPersonDto() {
        Person data = new Person(0L, "test1", "test2", null);
        PersonDto expected = new PersonDto("test1", "test2");

        PersonDto actual = underTest.getPersonDto(data);
        assertEquals(expected, actual);
    }
}