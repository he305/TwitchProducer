package com.github.he305.twitchproducer.application.dto;

import com.github.he305.twitchproducer.common.entities.Streamer;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class StreamerListDto {
    List<Streamer> streamers;
}
