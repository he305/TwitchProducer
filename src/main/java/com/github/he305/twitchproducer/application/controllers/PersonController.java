package com.github.he305.twitchproducer.application.controllers;

import com.github.he305.twitchproducer.application.constants.ApiVersionPathConstants;
import com.github.he305.twitchproducer.application.dto.PersonDtoListDto;
import com.github.he305.twitchproducer.application.dto.PersonResponseDto;
import com.github.he305.twitchproducer.common.dao.PersonDao;
import com.github.he305.twitchproducer.common.dto.PersonAddDto;
import com.github.he305.twitchproducer.common.exception.EntityExistsException;
import com.github.he305.twitchproducer.common.exception.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping(ApiVersionPathConstants.V1)
@RequiredArgsConstructor
public class PersonController {
    private final PersonDao personDao;

    @GetMapping("/person")
    public PersonDtoListDto getAllPersons() {
        List<PersonResponseDto> personList = personDao.getAll();
        return new PersonDtoListDto(personList);
    }

    @GetMapping("/person/{personId}")
    public ResponseEntity<PersonResponseDto> getPersonById(@PathVariable Long personId) {
        Optional<PersonResponseDto> personDto = personDao.getPersonById(personId);
        return personDto
                .map(dto -> new ResponseEntity<>(dto, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));

    }

    @PostMapping("/person")
    public ResponseEntity<PersonResponseDto> addPerson(@RequestBody PersonAddDto personAddDto) {
        try {
            PersonResponseDto saved = personDao.addPerson(personAddDto);
            return new ResponseEntity<>(saved, HttpStatus.CREATED);
        } catch (EntityExistsException exception) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @DeleteMapping("/person/{personId}")
    public ResponseEntity<String> deletePerson(@PathVariable Long personId) {
        try {
            personDao.deletePerson(personId);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (EntityNotFoundException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping("/person/{personId}")
    public ResponseEntity<PersonResponseDto> updatePerson(@PathVariable Long personId, @RequestBody PersonAddDto dto) {
        try {
            PersonResponseDto res = personDao.updatePerson(personId, dto);
            return new ResponseEntity<>(res, HttpStatus.OK);
        } catch (EntityNotFoundException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }
}
