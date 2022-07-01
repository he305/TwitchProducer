package com.github.he305.twitchproducer.application.mapper;

import com.github.he305.twitchproducer.common.dto.PersonAddDto;
import com.github.he305.twitchproducer.common.entities.Person;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.HashSet;

import static org.junit.jupiter.api.Assertions.assertEquals;

class PersonAddMapperImplTest {

    private PersonAddMapperImpl underTest;

    @BeforeEach
    void setUp() {
        underTest = new PersonAddMapperImpl();
    }


    @Test
    void getPerson() {
        PersonAddDto data = new PersonAddDto(
                "test1",
                "test2"
        );

        Person expected = new Person(
                null,
                "test1",
                "test2",
                new HashSet<>()
        );

        Person actual = underTest.getPerson(data);
        assertEquals(expected, actual);
    }
}