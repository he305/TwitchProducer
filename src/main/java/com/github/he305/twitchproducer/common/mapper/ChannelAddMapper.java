package com.github.he305.twitchproducer.common.mapper;

import com.github.he305.twitchproducer.common.dto.ChannelAddDto;
import com.github.he305.twitchproducer.common.entities.Channel;

public interface ChannelAddMapper {
    Channel getChannel(ChannelAddDto dto);
}
