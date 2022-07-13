package com.github.he305.twitchproducer.application.mapper;

import com.github.he305.twitchproducer.common.dto.StreamDataResponseDto;
import com.github.he305.twitchproducer.common.dto.StreamResponseDto;
import com.github.he305.twitchproducer.common.entities.Stream;
import com.github.he305.twitchproducer.common.mapper.StreamDataResponseMapper;
import com.github.he305.twitchproducer.common.mapper.StreamResponseMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class StreamResponseMapperImpl implements StreamResponseMapper {
    private final StreamDataResponseMapper dataMapper;

    @Override
    public StreamResponseDto toDto(Stream stream) {
        List<StreamDataResponseDto> streamData = stream.getStreamData().stream()
                .map(dataMapper::toDto)
                .collect(Collectors.toList());

        return new StreamResponseDto(
                stream.getId(),
                stream.getStartedAt(),
                stream.getEndedAt(),
                stream.getMaxViewers(),
                stream.getChannel().getId(),
                streamData
        );
    }
}
