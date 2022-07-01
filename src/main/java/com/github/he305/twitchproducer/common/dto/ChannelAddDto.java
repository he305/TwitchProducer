package com.github.he305.twitchproducer.common.dto;

import com.github.he305.twitchproducer.common.entities.Platform;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ChannelAddDto {
    private String nickname;
    private Platform platform;
}
