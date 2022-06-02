package com.github.he305.TwitchProducer.application.dto;

import com.github.he305.TwitchProducer.common.entities.Streamer;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class StreamerListDto {
    List<Streamer> streamers;
}
