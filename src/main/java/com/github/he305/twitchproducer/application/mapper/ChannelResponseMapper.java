package com.github.he305.twitchproducer.application.mapper;

import com.github.he305.twitchproducer.common.dto.ChannelResponseDto;
import com.github.he305.twitchproducer.common.entities.Channel;
import org.springframework.stereotype.Component;

@Component
public class ChannelResponseMapper {
    public ChannelResponseDto toDto(Channel channel) {
        return new ChannelResponseDto(
                channel.getId(),
                channel.getNickname(),
                channel.getPlatform(),
                channel.getPerson().getFullName()
        );
    }
}
