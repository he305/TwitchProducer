package com.github.he305.twitchproducer.application.dto;

import com.github.he305.twitchproducer.common.dto.StreamResponseDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class StreamListDto {
    private List<StreamResponseDto> streams;
}
