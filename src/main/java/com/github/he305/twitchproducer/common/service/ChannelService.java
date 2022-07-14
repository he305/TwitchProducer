package com.github.he305.twitchproducer.common.service;

import com.github.he305.twitchproducer.common.dto.ChannelAddDto;
import com.github.he305.twitchproducer.common.dto.ChannelResponseDto;
import com.github.he305.twitchproducer.common.exception.EntityNotFoundException;
import lombok.NonNull;

import java.util.List;
import java.util.Optional;

public interface ChannelService {
    List<ChannelResponseDto> getAllChannels();

    Optional<ChannelResponseDto> getChannelByName(@NonNull String nickname);

    List<ChannelResponseDto> getLiveChannels();

    Optional<ChannelResponseDto> getPersonChannelByName(@NonNull Long personId, @NonNull String channelName);

    Optional<ChannelResponseDto> getChannelById(@NonNull Long id);

    ChannelResponseDto addChannel(@NonNull Long personId, @NonNull ChannelAddDto channelAddDto);

    void deleteChannel(Long channelId) throws EntityNotFoundException;

    ChannelResponseDto updateChannel(Long channelId, ChannelAddDto dto) throws EntityNotFoundException;
}
