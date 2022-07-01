package com.github.he305.twitchproducer.application.dto;

import com.github.he305.twitchproducer.common.dto.PersonResponseDto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PersonDtoListDto {
    private List<PersonResponseDto> persons;
}
