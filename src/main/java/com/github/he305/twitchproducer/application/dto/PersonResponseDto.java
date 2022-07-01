package com.github.he305.twitchproducer.application.dto;

import com.github.he305.twitchproducer.common.dto.ChannelResponseDto;
import lombok.*;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
public class PersonResponseDto {
    private Long id;
    private String firstName;
    private String lastName;
    private List<ChannelResponseDto> channels;
}
