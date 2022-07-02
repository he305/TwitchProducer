package com.github.he305.twitchproducer.application.dto;

import com.github.he305.twitchproducer.common.dto.StreamResponseDto;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class StreamListDto {
    private List<StreamResponseDto> streams;
}
