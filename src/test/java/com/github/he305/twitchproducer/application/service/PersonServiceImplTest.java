package com.github.he305.twitchproducer.application.service;

import com.github.he305.twitchproducer.application.dto.PersonResponseDto;
import com.github.he305.twitchproducer.common.dao.PersonDao;
import com.github.he305.twitchproducer.common.dto.PersonAddDto;
import com.github.he305.twitchproducer.common.entities.Person;
import com.github.he305.twitchproducer.common.exception.EntityAlreadyExistsException;
import com.github.he305.twitchproducer.common.exception.EntityNotFoundException;
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
    private PersonDao personDao;
    @Mock
    private PersonResponseMapper personResponseMapper;
    @Mock
    private PersonAddMapper personAddMapper;

    private PersonServiceImpl underTest;

    @BeforeEach
    void setUp() {
        underTest = new PersonServiceImpl(personDao, personResponseMapper, personAddMapper);
    }

    @Test
    void getAll() {
        Person person = new Person(0L, "test1", "test2", null);
        PersonResponseDto personResponseDto = new PersonResponseDto(0L, "test1", "test2", null);
        List<PersonResponseDto> expected = List.of(personResponseDto);
        Mockito.when(personDao.getAll()).thenReturn(List.of(person));
        Mockito.when(personResponseMapper.getPersonDto(person)).thenReturn(personResponseDto);

        List<PersonResponseDto> actual = underTest.getAll();
        assertEquals(expected.size(), actual.size());
        assertEquals(actual, expected);
    }

    @Test
    void getPersonById_existing() {
        Long personId = 0L;
        Mockito.when(personResponseMapper.getPersonDto(Mockito.any())).thenReturn(new PersonResponseDto());
        Mockito.when(personDao.get(Mockito.anyLong())).thenReturn(Optional.of(new Person()));

        Optional<PersonResponseDto> actual = underTest.getPersonById(personId);
        assertTrue(actual.isPresent());
    }

    @Test
    void getPersonById_nonExistent() {
        Long personId = 0L;
        Mockito.when(personDao.get(personId)).thenReturn(Optional.empty());

        Optional<PersonResponseDto> actual = underTest.getPersonById(personId);
        assertTrue(actual.isEmpty());
    }

    @Test
    void addPerson_alreadyExist() {
        Mockito.when(personAddMapper.getPerson(Mockito.any())).thenReturn(new Person());
        Mockito.when(personDao.getByLastName(Mockito.any())).thenReturn(Optional.of(new Person()));

        PersonAddDto data = new PersonAddDto("", "");

        assertThrows(EntityAlreadyExistsException.class, () -> underTest.addPerson(data));
    }

    @Test
    void addPerson_success() {
        Person person = new Person();

        Mockito.when(personAddMapper.getPerson(Mockito.any())).thenReturn(person);
        Mockito.when(personDao.getByLastName(Mockito.any())).thenReturn(Optional.empty());
        Mockito.when(personDao.save(Mockito.any())).thenReturn(person);

        PersonAddDto data = new PersonAddDto();
        assertDoesNotThrow(() -> underTest.addPerson(data));
    }

    @Test
    void deletePerson_notFound() {
        Mockito.when(personDao.get(Mockito.anyLong())).thenReturn(Optional.empty());
        assertThrows(EntityNotFoundException.class, () ->
                underTest.deletePerson(0L));
    }

    @Test
    void deletePerson_success() {
        Mockito.when(personDao.get(Mockito.anyLong())).thenReturn(Optional.of(new Person()));
        assertDoesNotThrow(() -> underTest.deletePerson(0L));
    }

    @Test
    void updatePerson_notFound() {
        PersonAddDto dto = new PersonAddDto();
        Mockito.when(personDao.get(Mockito.anyLong())).thenReturn(Optional.empty());
        assertThrows(EntityNotFoundException.class, () ->
                underTest.updatePerson(0L, dto));
    }

    @Test
    void updatePerson_success() {
        PersonAddDto dto = new PersonAddDto(
                "new",
                "name"
        );
        Person existing = new Person(
                0L,
                "old",
                "old",
                null
        );
        Person changed = new Person(
                0L,
                "new",
                "name",
                null
        );
        PersonResponseDto expected = new PersonResponseDto(
                0L,
                "new",
                "name",
                null
        );

        Mockito.when(personDao.get(0L)).thenReturn(Optional.of(existing));
        Mockito.when(personDao.save(changed)).thenReturn(changed);
        Mockito.when(personResponseMapper.getPersonDto(changed)).thenReturn(expected);
        PersonResponseDto actual = underTest.updatePerson(0L, dto);
        assertEquals(actual, expected);
    }
}