package com.github.he305.twitchproducer.application.dao;

import com.github.he305.twitchproducer.application.repositories.PersonRepository;
import com.github.he305.twitchproducer.common.entities.Person;
import com.github.he305.twitchproducer.common.exception.EntitySaveFailedException;
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
    void get_existing() {
        Long personId = 0L;
        Mockito.when(personRepository.findById(Mockito.anyLong())).thenReturn(Optional.of(new Person()));
        Optional<Person> actual = underTest.get(personId);
        assertTrue(actual.isPresent());
    }

    @Test
    void get_nonExistent() {
        Long personId = 0L;
        Mockito.when(personRepository.findById(personId)).thenReturn(Optional.empty());
        Optional<Person> actual = underTest.get(personId);
        assertTrue(actual.isEmpty());
    }

    @Test
    void save_shouldThrow() {
        Mockito.when(personRepository.save(Mockito.any())).thenThrow(new DataAccessResourceFailureException("sad"));
        Person data = new Person();
        assertThrows(EntitySaveFailedException.class, () -> underTest.save(data));
    }

    @Test
    void save_success() {
        Person person = new Person();
        Mockito.when(personRepository.save(Mockito.any())).thenReturn(person);
        Person data = new Person();
        assertDoesNotThrow(() -> underTest.save(data));
    }

    @Test
    void delete_notFound() {
        Person data = new Person();
        doThrow(new DataAccessResourceFailureException("sad")).when(personRepository).delete(Mockito.any());
        assertThrows(DataAccessException.class, () ->
                underTest.delete(data));
    }

    @Test
    void delete_success() {
        doNothing().when(personRepository).delete(Mockito.any());
        assertDoesNotThrow(() -> underTest.delete(new Person()));
    }

    @Test
    void findByLastName_notFound() {
        Mockito.when(personRepository.findByLastName(Mockito.any())).thenReturn(Optional.empty());
        Optional<Person> actual = underTest.getByLastName("123");
        assertTrue(actual.isEmpty());
    }

    @Test
    void findByLastName_found() {
        Person expected = new Person();
        Mockito.when(personRepository.findByLastName(Mockito.any())).thenReturn(Optional.of(expected));
        Optional<Person> actual = underTest.getByLastName("123");
        assertTrue(actual.isPresent());
        assertEquals(expected, actual.get());
    }
}