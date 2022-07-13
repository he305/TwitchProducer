package com.github.he305.twitchproducer.application.dao;

import com.github.he305.twitchproducer.application.repositories.PersonRepository;
import com.github.he305.twitchproducer.common.entities.Person;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.DataAccessResourceFailureException;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;

@ExtendWith(MockitoExtension.class)
class PersonDaoImplTest {

    @Mock
    private PersonRepository personRepository;

    private PersonDaoImpl underTest;

    @BeforeEach
    void setUp() {
        underTest = new PersonDaoImpl(personRepository);
    }

    @Test
    void getAll() {
        Person person = new Person(0L, "test1", "test2", null);
        List<Person> expected = List.of(person);
        Mockito.when(personRepository.findAll()).thenReturn(List.of(person));

        List<Person> actual = underTest.getAll();
        assertEquals(expected.size(), actual.size());
        assertEquals(actual, expected);
    }

    @Test
    void getPersonById_existing() {
        Long personId = 0L;
        Mockito.when(personRepository.findById(Mockito.anyLong())).thenReturn(Optional.of(new Person()));
        Optional<Person> actual = underTest.getPersonById(personId);
        assertTrue(actual.isPresent());
    }

    @Test
    void getPersonById_nonExistent() {
        Long personId = 0L;
        Mockito.when(personRepository.findById(personId)).thenReturn(Optional.empty());
        Optional<Person> actual = underTest.getPersonById(personId);
        assertTrue(actual.isEmpty());
    }

    @Test
    void savePerson_shouldThrow() {
        Mockito.when(personRepository.save(Mockito.any())).thenThrow(new DataAccessResourceFailureException("sad"));
        Person data = new Person();
        assertThrows(DataAccessException.class, () -> underTest.savePerson(data));
    }

    @Test
    void savePerson_success() {
        Person person = new Person();
        Mockito.when(personRepository.save(Mockito.any())).thenReturn(person);
        Person data = new Person();
        assertDoesNotThrow(() -> underTest.savePerson(data));
    }

    @Test
    void deletePerson_notFound() {
        Person data = new Person();
        doThrow(new DataAccessResourceFailureException("sad")).when(personRepository).delete(Mockito.any());
        assertThrows(DataAccessException.class, () ->
                underTest.deletePerson(data));
    }

    @Test
    void deletePerson_success() {
        doNothing().when(personRepository).delete(Mockito.any());
        assertDoesNotThrow(() -> underTest.deletePerson(new Person()));
    }

    @Test
    void findByLastName_notFound() {
        Mockito.when(personRepository.findByLastName(Mockito.any())).thenReturn(Optional.empty());
        Optional<Person> actual = underTest.findByLastName("123");
        assertTrue(actual.isEmpty());
    }

    @Test
    void findByLastName_found() {
        Person expected = new Person();
        Mockito.when(personRepository.findByLastName(Mockito.any())).thenReturn(Optional.of(expected));
        Optional<Person> actual = underTest.findByLastName("123");
        assertTrue(actual.isPresent());
        assertEquals(expected, actual.get());
    }
}