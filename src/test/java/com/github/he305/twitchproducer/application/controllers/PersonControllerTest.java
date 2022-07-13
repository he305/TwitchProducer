package com.github.he305.twitchproducer.application.controllers;

import com.github.he305.twitchproducer.application.dto.PersonDtoListDto;
import com.github.he305.twitchproducer.application.dto.PersonResponseDto;
import com.github.he305.twitchproducer.common.dto.PersonAddDto;
import com.github.he305.twitchproducer.common.exception.EntityAlreadyExistsException;
import com.github.he305.twitchproducer.common.exception.EntityNotFoundException;
import com.github.he305.twitchproducer.common.service.PersonService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PersonControllerTest {

    @Mock
    private PersonService personService;

    private PersonController underTest;

    @BeforeEach
    public void setUp() {
        underTest = new PersonController(personService);
    }

    @Test
    void getAllPersons() {
        List<PersonResponseDto> personResponseDtoList = List.of(
                new PersonResponseDto(0L, "", "", null),
                new PersonResponseDto(1L, "", "", null),
                new PersonResponseDto(2L, "", "", null)
        );

        Mockito.when(personService.getAll()).thenReturn(personResponseDtoList);
        PersonDtoListDto actual = underTest.getAllPersons();
        assertEquals(personResponseDtoList.size(), actual.getPersons().size());
    }

    @Test
    void getPersonById_existingEntry() {
        PersonResponseDto data = new PersonResponseDto(0L, "test1", "test2", null);
        Mockito.when(personService.getPersonById(Mockito.anyLong())).thenReturn(Optional.of(data));
        ResponseEntity<PersonResponseDto> actual = underTest.getPersonById(0L);
        assertEquals(HttpStatus.OK, actual.getStatusCode());
        assertEquals(data, actual.getBody());
    }

    @Test
    void getPersonByLastName_notExistingEntry() {
        PersonResponseDto data = new PersonResponseDto(0L, "test1", "test2", null);
        Mockito.when(personService.getPersonById(Mockito.anyLong())).thenReturn(Optional.empty());
        ResponseEntity<PersonResponseDto> actual = underTest.getPersonById(0L);
        assertEquals(HttpStatus.NOT_FOUND, actual.getStatusCode());
    }

    @Test
    void addPerson_success() {
        PersonAddDto data = new PersonAddDto("test1", "test2");
        PersonResponseDto expected = new PersonResponseDto(0L, "test1", "test2", null);
        Mockito.when(personService.addPerson(Mockito.any())).thenReturn(expected);
        ResponseEntity<PersonResponseDto> actual = underTest.addPerson(data);
        assertEquals(HttpStatus.CREATED, actual.getStatusCode());
        assertEquals(expected, actual.getBody());
    }

    @Test
    void addPerson_alreadyExist() {
        PersonAddDto data = new PersonAddDto("test1", "test2");
        Mockito.when(personService.addPerson(Mockito.any())).thenThrow(EntityAlreadyExistsException.class);
        ResponseEntity<PersonResponseDto> actual = underTest.addPerson(data);
        assertEquals(HttpStatus.BAD_REQUEST, actual.getStatusCode());
    }

    @Test
    void deletePerson_notFound() {
        doThrow(new EntityNotFoundException()).when(personService).deletePerson(Mockito.anyLong());
        ResponseEntity<String> actual = underTest.deletePerson(0L);
        assertEquals(HttpStatus.BAD_REQUEST, actual.getStatusCode());
    }

    @Test
    void deletePerson_success() {
        doNothing().when(personService).deletePerson(Mockito.anyLong());
        ResponseEntity<String> actual = underTest.deletePerson(0L);
        assertEquals(HttpStatus.NO_CONTENT, actual.getStatusCode());
    }

    @Test
    void updatePerson_notFound() {
        PersonAddDto dto = new PersonAddDto();
        doThrow(new EntityNotFoundException()).when(personService).updatePerson(0L, dto);
        ResponseEntity<PersonResponseDto> actual = underTest.updatePerson(0L, dto);
        assertEquals(HttpStatus.BAD_REQUEST, actual.getStatusCode());
    }

    @Test
    void updatePerson_valid() {
        PersonAddDto dto = new PersonAddDto();
        PersonResponseDto expected = new PersonResponseDto(
                0L,
                "test",
                "test",
                null
        );
        doReturn(expected).when(personService).updatePerson(0L, dto);
        ResponseEntity<PersonResponseDto> actual = underTest.updatePerson(0L, dto);
        assertEquals(HttpStatus.OK, actual.getStatusCode());
        assertEquals(expected, actual.getBody());
    }
}