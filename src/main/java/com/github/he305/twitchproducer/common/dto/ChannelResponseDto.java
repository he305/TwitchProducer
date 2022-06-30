package com.github.he305.twitchproducer.common.dto;

import com.github.he305.twitchproducer.common.entities.Platform;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@EqualsAndHashCode
public class ChannelResponseDto {
    private Long id;
    private String nickname;
    private Platform platform;
    private String personFullName;
}
