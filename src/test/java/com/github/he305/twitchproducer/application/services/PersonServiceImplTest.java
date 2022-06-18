package com.github.he305.twitchproducer.application.services;

import com.github.he305.twitchproducer.application.repositories.PersonRepository;
import com.github.he305.twitchproducer.common.dto.PersonDto;
import com.github.he305.twitchproducer.common.entities.Person;
import com.github.he305.twitchproducer.common.exception.EntityExistsException;
import com.github.he305.twitchproducer.common.mapper.PersonMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class PersonServiceImplTest {

    @Mock
    private PersonRepository personRepository;
    @Mock
    private PersonMapper personMapper;

    private PersonServiceImpl underTest;

    @BeforeEach
    void setUp() {
        underTest = new PersonServiceImpl(personRepository, personMapper);
    }

    @Test
    void getAll() {
        Person person = new Person(0L, "test1", "test2", null);
        PersonDto personDto = new PersonDto("test1", "test2");
        Mockito.when(personRepository.findAll()).thenReturn(List.of(person));
        Mockito.when(personMapper.getPersonDto(person)).thenReturn(personDto);

        List<PersonDto> actual = underTest.getAll();
        assertEquals(1, actual.size());
    }

    @Test
    void getPersonByLastName_existing() {
        String data = "test";
        Mockito.when(personMapper.getPersonDto(Mockito.any())).thenReturn(new PersonDto());
        Mockito.when(personRepository.findByLastName(Mockito.any())).thenReturn(Optional.of(new Person()));

        Optional<PersonDto> actual = underTest.getPersonByLastName(data);
        assertTrue(actual.isPresent());
    }

    @Test
    void getPersonByLastName_nonExistent() {
        String data = "test";
        Mockito.when(personRepository.findByLastName(data)).thenReturn(Optional.empty());

        Optional<PersonDto> actual = underTest.getPersonByLastName(data);
        assertTrue(actual.isEmpty());
    }

    @Test
    void addPerson_alreadyExist() {
        Mockito.when(personMapper.getPerson(Mockito.any())).thenReturn(new Person());
        Mockito.when(personRepository.findByLastName(Mockito.any())).thenReturn(Optional.of(new Person()));

        PersonDto data = new PersonDto("", "");

        assertThrows(EntityExistsException.class, () -> underTest.addPerson(data));
    }

    @Test
    void addPerson_success() {
        Person person = new Person();

        Mockito.when(personMapper.getPerson(Mockito.any())).thenReturn(person);
        Mockito.when(personRepository.findByLastName(Mockito.any())).thenReturn(Optional.empty());
        Mockito.when(personRepository.save(Mockito.any())).thenReturn(person);

        PersonDto data = new PersonDto();
        assertDoesNotThrow(() -> underTest.addPerson(data));
    }
}