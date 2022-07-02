package com.github.he305.twitchproducer.application.mapper;

import com.github.he305.twitchproducer.common.dto.ChannelAddDto;
import com.github.he305.twitchproducer.common.entities.Channel;
import com.github.he305.twitchproducer.common.mapper.ChannelAddMapper;
import org.springframework.stereotype.Component;

import java.util.Collections;

@Component
public class ChannelAddMapperImpl implements ChannelAddMapper {
    @Override
    public Channel getChannel(ChannelAddDto dto) {
        return new Channel(
                null,
                dto.getNickname(),
                dto.getPlatform(),
                null,
                Collections.emptyList()
        );
    }
}
