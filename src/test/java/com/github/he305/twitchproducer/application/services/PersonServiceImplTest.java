package com.github.he305.twitchproducer.application.services;

import com.github.he305.twitchproducer.application.repositories.PersonRepository;
import com.github.he305.twitchproducer.common.dto.PersonAddDto;
import com.github.he305.twitchproducer.common.dto.PersonResponseDto;
import com.github.he305.twitchproducer.common.entities.Person;
import com.github.he305.twitchproducer.common.exception.EntityExistsException;
import com.github.he305.twitchproducer.common.mapper.PersonAddMapper;
import com.github.he305.twitchproducer.common.mapper.PersonResponseMapper;
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
    private PersonResponseMapper personResponseMapper;
    @Mock
    private PersonAddMapper personAddMapper;

    private PersonServiceImpl underTest;

    @BeforeEach
    void setUp() {
        underTest = new PersonServiceImpl(personRepository, personResponseMapper, personAddMapper);
    }

    @Test
    void getAll() {
        Person person = new Person(0L, "test1", "test2", null);
        PersonResponseDto personResponseDto = new PersonResponseDto(0L, "test1", "test2");
        List<PersonResponseDto> expected = List.of(personResponseDto);
        Mockito.when(personRepository.findAll()).thenReturn(List.of(person));
        Mockito.when(personResponseMapper.getPersonDto(person)).thenReturn(personResponseDto);

        List<PersonResponseDto> actual = underTest.getAll();
        assertEquals(expected.size(), actual.size());
        assertEquals(actual, expected);
    }

    @Test
    void getPersonByLastName_existing() {
        String data = "test";
        Mockito.when(personResponseMapper.getPersonDto(Mockito.any())).thenReturn(new PersonResponseDto());
        Mockito.when(personRepository.findByLastName(Mockito.any())).thenReturn(Optional.of(new Person()));

        Optional<PersonResponseDto> actual = underTest.getPersonByLastName(data);
        assertTrue(actual.isPresent());
    }

    @Test
    void getPersonByLastName_nonExistent() {
        String data = "test";
        Mockito.when(personRepository.findByLastName(data)).thenReturn(Optional.empty());

        Optional<PersonResponseDto> actual = underTest.getPersonByLastName(data);
        assertTrue(actual.isEmpty());
    }

    @Test
    void addPerson_alreadyExist() {
        Mockito.when(personAddMapper.getPerson(Mockito.any())).thenReturn(new Person());
        Mockito.when(personRepository.findByLastName(Mockito.any())).thenReturn(Optional.of(new Person()));

        PersonAddDto data = new PersonAddDto("", "");

        assertThrows(EntityExistsException.class, () -> underTest.addPerson(data));
    }

    @Test
    void addPerson_success() {
        Person person = new Person();

        Mockito.when(personAddMapper.getPerson(Mockito.any())).thenReturn(person);
        Mockito.when(personRepository.findByLastName(Mockito.any())).thenReturn(Optional.empty());
        Mockito.when(personRepository.save(Mockito.any())).thenReturn(person);

        PersonAddDto data = new PersonAddDto();
        assertDoesNotThrow(() -> underTest.addPerson(data));
    }
}