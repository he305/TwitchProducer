package com.github.he305.twitchproducer.common.dao;

import com.github.he305.twitchproducer.common.entities.Channel;
import lombok.NonNull;

import java.util.Optional;

public interface ChannelDao extends Dao<Channel> {
    Optional<Channel> getChannelByName(@NonNull String nickname);
}
